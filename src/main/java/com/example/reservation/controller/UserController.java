package com.example.reservation.controller;

import com.example.reservation.dto.form.*;
import com.example.reservation.dto.response.*;
import com.example.reservation.exception.FormException;
import com.example.reservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //개인 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid SignUpForm signUpForm, Errors errors) {
        errorCheck(errors);
        return ResponseEntity.ok(userService.signUp(signUpForm));
    }

    //개인 로그인
    @GetMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInForm signInForm, Errors errors) {
        errorCheck(errors);
        return ResponseEntity.ok(userService.signIn(signInForm));
    }

    //상점 이름으로 검색
    @GetMapping("/search")
    public ResponseEntity<List<SearchResponse>> search(@RequestParam String name) {
        return ResponseEntity.ok(userService.search(name));
    }

    //상점 아이디로 상세정보 검색(리뷰를 포함한 상점의 모든 정보 제공)
    @GetMapping("/get-detail")
    public ResponseEntity<StoreResponse> getDetail(@RequestParam Long storeId) {
        return ResponseEntity.ok(userService.getDetail(storeId));
    }

    //예약하기
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationResponse> reserve(
            @RequestHeader(name = "TOKEN") String token,
            @RequestBody @Valid ReservationForm reservationForm,
            Errors errors
    ) {
        errorCheck(errors);
        return ResponseEntity.ok(userService.reserve(token, reservationForm));
    }

    //개인 회원이 예약한 모든 내역 리스트
    @GetMapping("/get-reserves")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReservationResponse>> searchReserve(@RequestHeader(name = "TOKEN") String token) {
        return ResponseEntity.ok(userService.getReserves(token));
    }

    //예약 아이디로 예약 취소
    @PutMapping("/cancel-reserve")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelReserve(@RequestHeader(name = "TOKEN") String token, @RequestParam Long reserveId) {
        userService.cancelReserve(token, reserveId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    //방문확인
    @PutMapping("/arrive")
    public ResponseEntity<String> arrive(@RequestBody @Valid ArriveForm arriveForm, Errors errors) {
        errorCheck(errors);
        userService.arrive(arriveForm);
        return ResponseEntity.ok("방문이 확인되었습니다.");
    }

    //상점 아이디를 통해 리뷰 남기기
    @PostMapping("/review")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponse> review(
            @RequestHeader(name = "TOKEN") String token,
            @RequestBody @Valid ReviewForm reviewForm,
            Errors errors
    ) {
        errorCheck(errors);
        return ResponseEntity.ok(userService.review(token, reviewForm));
    }

    //개인 회원이 작성한 모든 리뷰 리스트
    @GetMapping("/get-reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReviewResponse>> getReviews(@RequestHeader(name = "TOKEN") String token) {
        return ResponseEntity.ok(userService.getReviews(token));
    }

    //리뷰 아이디를 통해 본인이 작성한 리뷰 수정
    @PutMapping("/update-review")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponse> updateReview(
            @RequestHeader(name = "TOKEN") String token,
            @RequestBody @Valid ReviewUpdateForm reviewUpdateForm,
            Errors errors
    ) {
        errorCheck(errors);
        return ResponseEntity.ok(userService.updateReview(token, reviewUpdateForm));
    }

    //리뷰 아이디를 이용해 본인이 작성한 리뷰 삭제
    @DeleteMapping("/delete-review")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteReview(@RequestHeader(name = "TOKEN") String token, @RequestParam Long reviewId) {
        userService.deleteReview(token, reviewId);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }

    //RequestBody 로 받는 각종 Form 의 Valid 체크
    private void errorCheck(Errors errors) {
        if (errors.getErrorCount() > 0) {
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError e : errors.getAllErrors()) {errorMessage.append(e.getDefaultMessage()).append("\n");}
            throw new FormException(errorMessage.toString());
        }
    }
}
