package com.example.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    FORM_NOT_VALID("올바른 양식이 아닙니다."),

    ID_EXIST("사용중인 아이디입니다."),
    ID_PASSWORD_NOT_VALID("아이디, 비밀번호를 확인해주세요"),
    STORE_NOT_FOUND("상점 정보를 찾을수 없습니다."),
    RESERVE_NOT_FOUND("예약 정보를 찾을수 없습니다."),
    REVIEW_NOT_FOUND("리뷰를 찾을수 없습니다."),

    PARTNER_NOT_FOUND("파트너 회원을 찾을수 없습니다."),
    STORE_NOT_ADDED("소유주로 등록된 상점이 없습니다."),
    STORE_PARTNER_NOT_MATCH("상점의 소유주가 아닙니다."),
    RESERVE_PARTNER_NOT_MATCH("예약된 상점의 소유주가 아닙니다."),
    RESERVE_NOT_WAIT("확정 대기중인 예약이 아닙니다."),
    REVIEW_PARTNER_NOT_MATCH("리뷰가 등록된 상점의 소유주가 아닙니다."),

    USER_NOT_FOUNT("개인 회원을 찾을수 없습니다."),
    SEARCH_NOT_FOUND("검색된 상점이 없습니다."),
    RESERVE_BEFORE_DATE("과거일자의 예약은 불가능합니다."),
    RESERVE_MUST_30M("예약시간을 30분 단위로 입력해주세요."),
    RESERVE_TIME_INVALID("해당 상점에 예약 가능한 시간이 아닙니다."),
    RESERVE_EXIST("해당 상점에 확정 또는 대기중인 예약이 존재합니다."),
    RESERVE_FULL("해당 일시에 가능한 테이블이 모두 예약되었습니다."),
    RESERVE_NOT_MADE("예약한 내역이 없습니다."),
    RESERVE_USER_NOT_MATCH("예약한 회원이 아닙니다."),
    RESERVE_CANCEL_IMPOSSIBLE("이미 취소되거나 완료된 예약입니다."),
    ARRIVE_NOT_ACCEPT("예약된 내역을 찾을수 없어 방문확인이 불가합니다."),
    ARRIVE_TIME_BEFORE("예약시간 10분 전부터 방문확인이 가능합니다."),
    ARRIVE_TIME_AFTER("예약시간이 10분 이상 경과되어 예약이 취소되었습니다."),
    REVIEW_HISTORY_NOT_FOUND("해당 상점을 방문확인 된 이력을 찾을수 없습니다."),
    REVIEW_NOT_MADE("작성한 리뷰가 없습니다."),
    REVIEW_USER_NOT_MATCH("리뷰를 작성한 회원이 아닙니다.");

    private final String message;
}
