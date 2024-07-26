package com.example.reservation.service;

import com.example.reservation.dto.form.ArriveForm;
import com.example.reservation.dto.form.ReservationForm;
import com.example.reservation.dto.form.ReviewForm;
import com.example.reservation.dto.form.ReviewUpdateForm;
import com.example.reservation.dto.form.SignInForm;
import com.example.reservation.dto.form.SignUpForm;
import com.example.reservation.dto.response.ReservationResponse;
import com.example.reservation.dto.response.ReviewResponse;
import com.example.reservation.dto.response.SearchResponse;
import com.example.reservation.dto.response.StoreResponse;
import com.example.reservation.dto.response.UserResponse;
import com.example.reservation.entity.ReservationEntity;
import com.example.reservation.entity.ReviewEntity;
import com.example.reservation.entity.StoreEntity;
import com.example.reservation.entity.UserEntity;
import com.example.reservation.exception.ErrorCode;
import com.example.reservation.exception.ServiceException;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.ReviewRepository;
import com.example.reservation.repository.StoreRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.SecurityManager;
import com.example.reservation.type.ReservationType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final ReservationRepository reservationRepository;
  private final ReviewRepository reviewRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecurityManager securityManager;


  @Transactional
  //개인 회원 가입(파트너와 동일)
  public UserResponse signUp(SignUpForm signUpForm) {
    String loginId = signUpForm.getLoginId();

    if (userRepository.findByLoginId(loginId).isPresent()) {
      throw new ServiceException(ErrorCode.ID_EXIST);
    }

    UserEntity userEntity = UserEntity.fromForm(signUpForm);
    userEntity.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    userEntity.setRole("ROLE_USER");

    return UserResponse.fromEntity(userRepository.save(userEntity));
  }

  @Transactional
  //개인 회원 로그인(파트너와 동일)
  public String signIn(SignInForm signInForm) {
    UserEntity userEntity = userRepository.findByLoginId(signInForm.getLoginId())
        .orElseThrow(() -> new ServiceException(ErrorCode.ID_PASSWORD_NOT_VALID));

    if (!securityManager.passCheck(signInForm.getPassword(), userEntity.getPassword())) {
      throw new ServiceException(ErrorCode.ID_PASSWORD_NOT_VALID);
    }

    return securityManager.tokenCreate(signInForm.getLoginId(), userEntity.getRole());
  }

  @Transactional
  public List<SearchResponse> search(String name) {
    //검색할 내용이 상점이름에 포함된 상점 엔티티 리스트
    List<StoreEntity> stores = storeRepository.findAllByStoreNameLike("%" + name + "%");

    //리스트가 비었으면 에러
    if (stores.isEmpty()) {
      throw new ServiceException(ErrorCode.SEARCH_NOT_FOUND);
    }

    //상점(검색용) Response 리스트로 변환하여 반환(상점의 리스트,아이디,이름,위치만 포함됨)
    return stores.stream().map(SearchResponse::fromEntity).toList();
  }

  @Transactional
  public StoreResponse getDetail(Long storeId) {
    //상점 아이디로 상점 찾기
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(() -> new ServiceException(ErrorCode.STORE_NOT_FOUND));

    //상점 엔티티를 Response 로 변환하여 반환(상점에 달린 리뷰 리스트도 들어있다)
    StoreResponse storeResponse = StoreResponse.fromEntity(storeEntity);
    List<ReviewResponse> reviewResponseList = storeEntity.getReviewEntityList().stream()
        .map(ReviewResponse::fromEntity).toList();
    storeResponse.setReviewResponseList(reviewResponseList);

    return storeResponse;
  }

  @Transactional
  public ReservationResponse reserve(String token, ReservationForm reservationForm) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //예약 폼에서 상점 아이디 받아와 상점 찾기
    StoreEntity storeEntity = storeRepository.findById(reservationForm.getStoreId())
        .orElseThrow(() -> new ServiceException(ErrorCode.STORE_NOT_FOUND));

    //폼에 있는 예약 날짜가 현재보다 과거면 에러(추가 구현)
    if (reservationForm.getReserveDate().isBefore(LocalDateTime.now())) {
      throw new ServiceException(ErrorCode.RESERVE_BEFORE_DATE);
    }

    //폼에 있는 예약 시간이 30분 단위가 아니면 에러(추가 구현)
    if (reservationForm.getReserveDate().getMinute() != 30
        && reservationForm.getReserveDate().getMinute() != 0) {
      throw new ServiceException(ErrorCode.RESERVE_MUST_30M);
    }

    //예약 시간이 상점 운영시간을 벗어나면 에러(추가 구현)
    int reserveTime =
        reservationForm.getReserveDate().getHour() * 60 + reservationForm.getReserveDate()
            .getMinute();
    int startTime =
        storeEntity.getStartTime().getHour() * 60 + storeEntity.getStartTime().getMinute();
    int endTime = storeEntity.getEndTime().getHour() * 60 + storeEntity.getEndTime().getMinute();

    if (reserveTime < startTime || reserveTime > endTime) {
      throw new ServiceException(ErrorCode.RESERVE_TIME_INVALID);
    }

    //회원이 해당 상점에 예약한 모든 내역 조회
    List<ReservationEntity> myReservations =
        reservationRepository.findAllByUserEntity_IdAndStoreEntity_Id(userEntity.getId(),
            storeEntity.getId());

    //리스트 중 이미 확정되었거나 확정 대기중인 예약이 있으면 에러(추가 구현)
    if (myReservations.stream().anyMatch(r ->
        r.getStatus() == ReservationType.RESERVED || r.getStatus() == ReservationType.WAIT)
    ) {
      throw new ServiceException(ErrorCode.RESERVE_EXIST);
    }

    //상점 아이디와 예약 날짜로 조회하여 해당 타임에 예약된 수가 상점등록시 설정한 타임당 예약수보다 적은지 확인, 꽉 찼으면 에러(추가 구현)
    List<ReservationEntity> timeReservations =
        reservationRepository.findAllByStoreEntity_IdAndReserveDate(storeEntity.getId(),
            reservationForm.getReserveDate());
    int reservedTableCount = (int) timeReservations.stream()
        .filter(r -> r.getStatus() == ReservationType.RESERVED).count();

    if (reservedTableCount >= storeEntity.getTablePerTime()) {
      throw new ServiceException(ErrorCode.RESERVE_FULL);
    }

    //예약 엔티티에 회원, 상점, 예약날짜 세팅, 예약상태를 확정대기(WAIT)으로 변경
    ReservationEntity reservationEntity = ReservationEntity.builder()
        .userEntity(userEntity)
        .storeEntity(storeEntity)
        .reserveDate(reservationForm.getReserveDate())
        .status(ReservationType.WAIT)
        .build();

    //예약된 엔티티를 Response 로 변환하여 반환
    return ReservationResponse.fromEntity(reservationRepository.save(reservationEntity));
  }

  @Transactional
  public List<ReservationResponse> getReserves(String token) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //해당 회원이 예약한 리스트
    List<ReservationEntity> reservationEntities = reservationRepository.findAllByUserEntity(
        userEntity);

    //리스트가 비었으면 에러
    if (reservationEntities.isEmpty()) {
      throw new ServiceException(ErrorCode.RESERVE_NOT_MADE);
    }

    //엔티티를 Response 로 변환한 리스트 반환
    return reservationEntities.stream().map(ReservationResponse::fromEntity).toList();
  }

  @Transactional
  public void cancelReserve(String token, Long reserveId) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //예약 아이디로 예약 찾기
    ReservationEntity reservationEntity = reservationRepository.findById(reserveId)
        .orElseThrow(() -> new ServiceException(ErrorCode.RESERVE_NOT_FOUND));

    //예약한 회원이 본인이 맞는지 확인
    if (!Objects.equals(reservationEntity.getUserEntity().getId(), userEntity.getId())) {
      throw new ServiceException(ErrorCode.RESERVE_USER_NOT_MATCH);
    }

    //예약확정 또는 확정대기 상태인 예약만 취소 가능(이미 취소됐거나 완료된 예약 취소 불가)
    if (reservationEntity.getStatus() != ReservationType.RESERVED &&
        reservationEntity.getStatus() != ReservationType.WAIT) {
      throw new ServiceException(ErrorCode.RESERVE_CANCEL_IMPOSSIBLE);
    }

    //예약 상태를 취소로 변경
    reservationEntity.setStatus(ReservationType.CANCEL);

  }

  @Transactional
  public void arrive(ArriveForm arriveForm) {
    //도착시 작성하는 폼에서 이름과 전화번호 받아서 유저 찾기
    UserEntity userEntity = userRepository.findByNameAndPhone(arriveForm.getUserName(),
            arriveForm.getUserPhone())
        .orElseThrow(() -> new ServiceException(ErrorCode.ARRIVE_NOT_ACCEPT));

    //회원이 도착하면 키오스크를 통해 이름,전화번호를 받아 상점 아이디(상점별로 미리 세팅되어 있어야함)와 함께 arriveForm 으로 넘긴다
    //arriveForm 안에 있는 상점 아이디로 상점 찾기
    StoreEntity storeEntity = storeRepository.findById(arriveForm.getStoreId())
        .orElseThrow(() -> new ServiceException(ErrorCode.ARRIVE_NOT_ACCEPT));

    //회원과 상점 정보를 통해 예약확정 상태인 예약을 조회
    List<ReservationEntity> reservationEntities = reservationRepository.findAllByUserEntityAndStoreEntity(
            userEntity, storeEntity)
        .stream().filter(r -> r.getStatus() == ReservationType.RESERVED).toList();

    //리스트가 비었으면 에러
    if (reservationEntities.isEmpty()) {
      throw new ServiceException(ErrorCode.ARRIVE_NOT_ACCEPT);
    }

    //리스트중 첫번째 가져오기(중복예약을 금지했기에 리스트 안에 하나만 있을것임, 방문완료시 새로 예약가능)
    ReservationEntity reservationEntity = reservationEntities.get(0);

    //예약시간 10분 전부터 예약가능(문제를 제대로 이해한게 맞는지?)
    if (reservationEntity.getReserveDate().minusMinutes(10).isAfter(LocalDateTime.now())) {
      throw new ServiceException(ErrorCode.ARRIVE_TIME_BEFORE);
    }

    //예약시간 이후로 10분이 경과되면 방문확인 불가(추가 구현)
    if (reservationEntity.getReserveDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
      throw new ServiceException(ErrorCode.ARRIVE_TIME_AFTER);
    }

    //방문확인이 완료되어 예약상태를 방문완료(END) 로 바꿈
    reservationEntity.setStatus(ReservationType.END);
  }

  @Transactional
  public ReviewResponse review(String token, ReviewForm reviewForm) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //리뷰 폼에 있는 상점 아이디로 상점 찾기
    StoreEntity storeEntity = storeRepository.findById(reviewForm.getStoreId())
        .orElseThrow(() -> new ServiceException(ErrorCode.STORE_NOT_FOUND));

    //회원과 상점 정보로 예약 내역 조회하여 그 중 방문완료(END) 상태인것만 리스트에 담기
    List<ReservationEntity> reservationEntities = reservationRepository.findAllByUserEntityAndStoreEntity(
            userEntity, storeEntity)
        .stream().filter(r -> r.getStatus() == ReservationType.END).toList();

    //리스트가 비었으면 에러
    if (reservationEntities.isEmpty()) {
      throw new ServiceException(ErrorCode.REVIEW_HISTORY_NOT_FOUND);
    }

    //방문완료된 예약이 여러건이면 첫번째를 가져와 해당 예약일(방문날짜)을 리뷰에 세팅해준다.
    LocalDateTime reserveDate = reservationEntities.get(0).getReserveDate();

    //리뷰 엔티티에 회원, 상점 정보, 리뷰내용, 예약일(방문날짜)를 세팅하고 만든다.
    ReviewEntity reviewEntity = ReviewEntity.builder()
        .userEntity(userEntity)
        .storeEntity(storeEntity)
        .content(reviewForm.getContent())
        .reserveDate(reserveDate)
        .build();

    //리뷰 엔티티를 Response 로 변환하여 반환
    return ReviewResponse.fromEntity(reviewRepository.save(reviewEntity));
  }

  @Transactional
  public List<ReviewResponse> getReviews(String token) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //회원이 작성한 모든 리뷰 리스트
    List<ReviewEntity> reviewEntityList = reviewRepository.findAllByUserEntity(userEntity);

    //리스트가 비었으면 에러
    if (reviewEntityList.isEmpty()) {
      throw new ServiceException(ErrorCode.REVIEW_NOT_MADE);
    }

    //리뷰 엔티티를 Response 로 변환한 리스트 반환
    return reviewEntityList.stream().map(ReviewResponse::fromEntity).toList();
  }

  @Transactional
  public ReviewResponse updateReview(String token, ReviewUpdateForm reviewUpdateForm) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //리뷰수정 폼으로 리뷰 아이디와 수정할 내용을 받고, 아이디로 리뷰 찾기
    ReviewEntity reviewEntity = reviewRepository.findById(reviewUpdateForm.getReviewId())
        .orElseThrow(() -> new ServiceException(ErrorCode.REVIEW_NOT_FOUND));

    //리뷰 작성자가 회원이 맞는지 확인
    if (!Objects.equals(reviewEntity.getUserEntity().getId(), userEntity.getId())) {
      throw new ServiceException(ErrorCode.REVIEW_USER_NOT_MATCH);
    }

    //리뷰의 내용을 폼에서 받은 내용으로 수정
    reviewEntity.setContent(reviewUpdateForm.getContent());

    //변경된 리뷰 Response 반환
    return ReviewResponse.fromEntity(reviewRepository.save(reviewEntity));
  }

  @Transactional
  public void deleteReview(String token, Long reviewId) {
    //토큰으로 회원 찾기
    UserEntity userEntity = userRepository.findByLoginId(securityManager.parseToken(token)).get();

    //리뷰 아이디로 리뷰 찾기
    ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ServiceException(ErrorCode.REVIEW_NOT_FOUND));

    //리뷰 작성자가 회원이 맞는지 확인
    if (!Objects.equals(reviewEntity.getUserEntity().getId(), userEntity.getId())) {
      throw new ServiceException(ErrorCode.REVIEW_USER_NOT_MATCH);
    }

    //리뷰 삭제
    reviewRepository.delete(reviewEntity);
  }
}
