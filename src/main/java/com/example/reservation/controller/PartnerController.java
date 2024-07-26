package com.example.reservation.controller;

import com.example.reservation.dto.form.SignInForm;
import com.example.reservation.dto.form.SignUpForm;
import com.example.reservation.dto.form.StoreForm;
import com.example.reservation.dto.response.PartnerResponse;
import com.example.reservation.dto.response.ReservationResponse;
import com.example.reservation.dto.response.StoreResponse;
import com.example.reservation.exception.FormException;
import com.example.reservation.service.PartnerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

  private final PartnerService partnerService;

  //파트너(점장) 회원가입
  @PostMapping("/signup")
  public ResponseEntity<PartnerResponse> signUp(@RequestBody @Valid SignUpForm signUpForm,
      Errors errors) {
    errorCheck(errors);
    return ResponseEntity.ok(partnerService.signUp(signUpForm));
  }

  //파트너(점장) 로그인
  @GetMapping("/signin")
  public ResponseEntity<String> signIn(@RequestBody @Valid SignInForm signInForm, Errors errors) {
    errorCheck(errors);
    return ResponseEntity.ok(partnerService.signIn(signInForm));
  }

  //상점 등록
  @PostMapping("/add-store")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<StoreResponse> addStore(@RequestHeader(name = "TOKEN") String token,
      @RequestBody @Valid StoreForm storeForm, Errors errors) {
    errorCheck(errors);
    return ResponseEntity.ok(partnerService.addStore(token, storeForm));
  }

  //소유한 모든 상점 리스트 확인
  @GetMapping("/get-stores")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<List<StoreResponse>> getStores(
      @RequestHeader(name = "TOKEN") String token) {
    return ResponseEntity.ok(partnerService.getStores(token));
  }

  //소유한 상점 정보 수정
  @PutMapping("/update-store")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<StoreResponse> updateStore(
      @RequestHeader(name = "TOKEN") String token,
      @RequestParam Long storeId,
      @RequestBody @Valid StoreForm storeForm,
      Errors errors
  ) {
    errorCheck(errors);
    return ResponseEntity.ok(partnerService.updateStore(token, storeId, storeForm));
  }

  //소유한 상점 삭제
  @DeleteMapping("/delete-store")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<String> deleteStore(@RequestHeader(name = "TOKEN") String token,
      @RequestParam Long storeId) {
    partnerService.deleteStore(token, storeId);
    return ResponseEntity.ok("상점 삭제 완료");
  }

  //소유한 상점의 예약내역 확인
  @GetMapping("/get-reserves")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<List<ReservationResponse>> getReserves(
      @RequestHeader(name = "TOKEN") String token, @RequestParam Long storeId) {
    return ResponseEntity.ok(partnerService.getReserves(token, storeId));
  }

  //상점의 예약 확정하기
  @PutMapping("/confirm-reserve")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<ReservationResponse> confirmReserve(
      @RequestHeader(name = "TOKEN") String token, @RequestParam Long reserveId) {
    return ResponseEntity.ok(partnerService.confirmReserve(token, reserveId));
  }

  //상점의 예약 거부하기(취소)
  @PutMapping("/cancel-reserve")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<ReservationResponse> cancelReserve(
      @RequestHeader(name = "TOKEN") String token, @RequestParam Long reserveId) {
    return ResponseEntity.ok(partnerService.cancelReserve(token, reserveId));
  }

  //상점에 등록된 리뷰 삭제
  @DeleteMapping("/delete-review")
  @PreAuthorize("hasRole('PARTNER')")
  public ResponseEntity<String> deleteReview(@RequestHeader(name = "TOKEN") String token,
      @RequestParam Long reviewId) {
    partnerService.deleteReview(token, reviewId);
    return ResponseEntity.ok("리뷰 삭제 완료");
  }

  //RequestBody 로 받는 각종 Form 의 Valid 체크
  private void errorCheck(Errors errors) {
    if (errors.getErrorCount() > 0) {
      StringBuilder errorMessage = new StringBuilder();
      for (ObjectError e : errors.getAllErrors()) {
        errorMessage.append(e.getDefaultMessage()).append("\n");
      }
      throw new FormException(errorMessage.toString());
    }
  }
}
