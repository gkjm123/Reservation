package com.example.reservation.service;

import com.example.reservation.dto.form.SignInForm;
import com.example.reservation.dto.form.SignUpForm;
import com.example.reservation.dto.form.StoreForm;
import com.example.reservation.dto.response.PartnerResponse;
import com.example.reservation.dto.response.ReservationResponse;
import com.example.reservation.dto.response.StoreResponse;
import com.example.reservation.entity.PartnerEntity;
import com.example.reservation.entity.ReservationEntity;
import com.example.reservation.entity.ReviewEntity;
import com.example.reservation.entity.StoreEntity;
import com.example.reservation.exception.ErrorCode;
import com.example.reservation.exception.ServiceException;
import com.example.reservation.repository.PartnerRepository;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.ReviewRepository;
import com.example.reservation.repository.StoreRepository;
import com.example.reservation.security.SecurityManager;
import com.example.reservation.type.ReservationType;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartnerService {

  private final PartnerRepository partnerRepository;
  private final StoreRepository storeRepository;
  private final ReviewRepository reviewRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecurityManager securityManager;
  private final ReservationRepository reservationRepository;


  @Transactional
  public PartnerResponse signUp(SignUpForm signUpForm) {
    String loginId = signUpForm.getLoginId();

    //로그인 아이디가 중복된게 있으면 에러 처리
    if (partnerRepository.findByLoginId(loginId).isPresent()) {
      throw new ServiceException(ErrorCode.ID_EXIST);
    }

    //회원가입 폼을 엔티티로 바꾸고 비밀번호는 encoding 해서 엔티티에 넣어준다.
    PartnerEntity partnerEntity = PartnerEntity.fromForm(signUpForm);
    partnerEntity.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    partnerEntity.setRole("ROLE_PARTNER");

    //엔티티를 Response 객체로 변환하여 반환한다
    return PartnerResponse.fromEntity(partnerRepository.save(partnerEntity));
  }

  @Transactional
  public String signIn(SignInForm signInForm) {
    //아이디가 존재하는지 확인
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(signInForm.getLoginId())
        .orElseThrow(() -> new ServiceException(ErrorCode.ID_PASSWORD_NOT_VALID));

    //비밀번호 일치 확인
    if (!securityManager.passCheck(signInForm.getPassword(), partnerEntity.getPassword())) {
      throw new ServiceException(ErrorCode.ID_PASSWORD_NOT_VALID);
    }

    //로그인 아이디와 역할을 넣어 생성된 토큰 반환
    return securityManager.tokenCreate(signInForm.getLoginId(), partnerEntity.getRole());
  }

  @Transactional
  public StoreResponse addStore(String token, StoreForm storeForm) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //폼을 엔티티로 변환하고, 파트너 엔티티는 직접 set 해준다.
    StoreEntity storeEntity = StoreEntity.fromForm(storeForm);
    storeEntity.setPartnerEntity(partnerEntity);

    //엔티티를 Response 객체로 변환해서 반환
    return StoreResponse.fromEntity(storeRepository.save(storeEntity));
  }

  @Transactional
  public List<StoreResponse> getStores(String token) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //파트너가 소유한 모든 상점 엔티티 리스트
    List<StoreEntity> storeEntityList = storeRepository.findAllByPartnerEntity(partnerEntity);

    //리스트가 비어있으면 에러
    if (storeEntityList.isEmpty()) {
      throw new ServiceException(ErrorCode.STORE_NOT_ADDED);
    }

    //상점 엔티티 리스트를 Response 리스트로 변환해서 반환
    return storeEntityList.stream().map(StoreResponse::fromEntity).toList();
  }

  @Transactional
  public StoreResponse updateStore(String token, Long storeId, StoreForm storeForm) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //상점 아이디로 상점 엔티티 찾기
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(() -> new ServiceException(ErrorCode.STORE_NOT_FOUND));

    //상점의 관리자 아이디와 조회하는 파트너 아이디 일치 확인
    if (!Objects.equals(storeEntity.getPartnerEntity().getId(), partnerEntity.getId())) {
      throw new ServiceException(ErrorCode.STORE_PARTNER_NOT_MATCH);
    }

    //상점 엔티티 필드값을 폼에서 가져온 것으로 바꾸기
    storeEntity.setStoreName(storeForm.getStoreName());
    storeEntity.setLocation(storeForm.getLocation());
    storeEntity.setDescription(storeForm.getDescription());
    storeEntity.setTablePerTime(storeForm.getTablePerTime());
    storeEntity.setStartTime(storeForm.getStartTime());
    storeEntity.setEndTime(storeForm.getEndTime());

    return StoreResponse.fromEntity(storeRepository.save(storeEntity));
  }

  @Transactional
  public void deleteStore(String token, Long storeId) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //상점 아이디로 상점 엔티티 찾기
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(() -> new ServiceException(ErrorCode.STORE_NOT_FOUND));

    //상점의 관리자 아이디와 조회하는 파트너 아이디 일치 확인
    if (!Objects.equals(storeEntity.getPartnerEntity().getId(), partnerEntity.getId())) {
      throw new ServiceException(ErrorCode.STORE_PARTNER_NOT_MATCH);
    }

    //해당 상점 엔티티 삭제
    storeRepository.delete(storeEntity);
  }

  @Transactional
  public List<ReservationResponse> getReserves(String token, Long storeId) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //상점 아이디로 상점 엔티티 찾기
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(() -> new ServiceException(ErrorCode.STORE_NOT_FOUND));

    //상점의 관리자 아이디와 조회하는 파트너 아이디 일치 확인
    if (!Objects.equals(storeEntity.getPartnerEntity().getId(), partnerEntity.getId())) {
      throw new ServiceException(ErrorCode.STORE_PARTNER_NOT_MATCH);
    }

    //상점에 연결된 모든 예약 찾아 리스트로
    List<ReservationEntity> reservationEntityList = reservationRepository.findAllByStoreEntity(
        storeEntity);

    //예약 리스트가 비어있으면 에러
    if (reservationEntityList.isEmpty()) {
      throw new ServiceException(ErrorCode.RESERVE_NOT_MADE);
    }

    //예약 Response 리스트 반환
    return reservationEntityList.stream().map(ReservationResponse::fromEntity).toList();
  }

  @Transactional
  public ReservationResponse confirmReserve(String token, Long reserveId) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //예약 아이디로 예약 엔티티 찾기
    ReservationEntity reservationEntity = reservationRepository.findById(reserveId)
        .orElseThrow(() -> new ServiceException(ErrorCode.RESERVE_NOT_FOUND));

    //예약된 상점의 관리자가 해당 파트너가 맞는지 확인
    if (!Objects.equals(reservationEntity.getStoreEntity().getPartnerEntity().getId(),
        partnerEntity.getId())) {
      throw new ServiceException(ErrorCode.RESERVE_PARTNER_NOT_MATCH);
    }

    //확정 대기중(WAIT) 상태가 아니면 예약확정 불가(이미 방문 완료했거나 사용자가 먼저 취소한 경우)
    if (reservationEntity.getStatus() != ReservationType.WAIT) {
      throw new ServiceException(ErrorCode.RESERVE_NOT_WAIT);
    }

    //예약의 상태를 예약확정(RESERVED)로 바꾸기
    reservationEntity.setStatus(ReservationType.RESERVED);

    return ReservationResponse.fromEntity(reservationRepository.save(reservationEntity));
  }

  @Transactional
  public ReservationResponse cancelReserve(String token, Long reserveId) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //예약 아이디로 예약 엔티티 찾기
    ReservationEntity reservationEntity = reservationRepository.findById(reserveId)
        .orElseThrow(() -> new ServiceException(ErrorCode.RESERVE_NOT_FOUND));

    //예약된 상점의 관리자가 해당 파트너가 맞는지 확인
    if (!Objects.equals(reservationEntity.getStoreEntity().getPartnerEntity().getId(),
        partnerEntity.getId())) {
      throw new ServiceException(ErrorCode.RESERVE_PARTNER_NOT_MATCH);
    }

    //확정 대기중(WAIT) 상태가 아니면 예약취소 불가(이미 방문 완료했거나 사용자가 먼저 취소한 경우)
    if (reservationEntity.getStatus() != ReservationType.WAIT) {
      throw new ServiceException(ErrorCode.RESERVE_NOT_WAIT);
    }

    //예약의 상태를 예약취소(CANCEL)로 바꾸기
    reservationEntity.setStatus(ReservationType.CANCEL);

    return ReservationResponse.fromEntity(reservationRepository.save(reservationEntity));
  }

  @Transactional
  public void deleteReview(String token, Long reviewId) {
    //토큰으로 파트너 회원 찾기
    PartnerEntity partnerEntity = partnerRepository.findByLoginId(securityManager.parseToken(token))
        .get();

    //리뷰 아이디로 리뷰 찾기
    ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ServiceException(ErrorCode.REVIEW_NOT_FOUND));

    //리뷰가 등록된 상점의 관리자가 해당 파트너가 맞는지 확인
    if (!Objects.equals(reviewEntity.getStoreEntity().getPartnerEntity().getId(),
        partnerEntity.getId())) {
      throw new ServiceException(ErrorCode.REVIEW_PARTNER_NOT_MATCH);
    }

    //리뷰 엔티티 삭제
    reviewRepository.delete(reviewEntity);
  }

}
