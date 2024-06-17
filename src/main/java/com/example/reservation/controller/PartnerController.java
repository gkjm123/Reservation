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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    //파트너(점장) 회원가입
    @PostMapping("/signup")
    public ResponseEntity<PartnerResponse> signUp(@RequestBody @Valid SignUpForm signUpForm, Errors errors) {
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
    public ResponseEntity<StoreResponse> addStore(@RequestHeader(name = "TOKEN") String token, @RequestBody @Valid StoreForm storeForm, Errors errors) {
        errorCheck(errors);
        return ResponseEntity.ok(partnerService.addStore(token, storeForm));
    }

    //소유한 모든 상점 리스트 확인
    @GetMapping("/get-stores")
    public ResponseEntity<List<StoreResponse>> getStores(@RequestHeader(name = "TOKEN") String token) {
        return ResponseEntity.ok(partnerService.getStores(token));
    }

    //소유한 상점 정보 수정
    @PutMapping("/update-store")
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
    public ResponseEntity<String> deleteStore(@RequestHeader(name = "TOKEN") String token, @RequestParam Long storeId) {
        partnerService.deleteStore(token, storeId);
        return ResponseEntity.ok("상점 삭제 완료");
    }

    //소유한 상점의 예약내역 확인
    @GetMapping("/get-reserves")
    public ResponseEntity<List<ReservationResponse>> getReserves(@RequestHeader(name = "TOKEN") String token, @RequestParam Long storeId) {
        return ResponseEntity.ok(partnerService.getReserves(token, storeId));
    }

    //상점의 예약 확정하기
    @PutMapping("/confirm-reserve")
    public ResponseEntity<ReservationResponse> confirmReserve(@RequestHeader(name = "TOKEN") String token, @RequestParam Long reserveId) {
        return ResponseEntity.ok(partnerService.confirmReserve(token, reserveId));
    }

    //상점의 예약 거부하기(취소)
    @PutMapping("/cancel-reserve")
    public ResponseEntity<ReservationResponse> cancelReserve(@RequestHeader(name = "TOKEN") String token, @RequestParam Long reserveId) {
        return ResponseEntity.ok(partnerService.cancelReserve(token, reserveId));
    }

    //상점에 등록된 리뷰 삭제
    @DeleteMapping("/delete-review")
    public ResponseEntity<String> deleteReview(@RequestHeader(name = "TOKEN") String token, @RequestParam Long reviewId) {
        partnerService.deleteReview(token, reviewId);
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
