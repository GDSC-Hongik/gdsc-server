package com.gdschongik.gdsc.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
    METHOD_ARGUMENT_NULL(HttpStatus.BAD_REQUEST, "인자는 null이 될 수 없습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "인자가 유효하지 않습니다."),
    REGEX_VIOLATION(HttpStatus.BAD_REQUEST, "정규표현식을 위반했습니다."),

    // Auth
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    AUTH_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보가 존재하지 않습니다."),
    AUTH_NOT_PARSABLE(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보 파싱에 실패했습니다."),

    // Parameter
    INVALID_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 쿼리 파라미터입니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    MEMBER_DELETED(HttpStatus.CONFLICT, "탈퇴한 회원입니다."),
    MEMBER_FORBIDDEN(HttpStatus.CONFLICT, "차단된 회원입니다."),

    // Requirement
    UNIV_NOT_VERIFIED(HttpStatus.CONFLICT, "재학생 인증이 되지 않았습니다."),

    // Univ Email Verification
    UNIV_EMAIL_ALREADY_VERIFIED(HttpStatus.CONFLICT, "이미 가입된 재학생 메일입니다."),
    UNIV_EMAIL_FORMAT_MISMATCH(HttpStatus.BAD_REQUEST, "형식에 맞지 않는 재학생 메일입니다."),
    UNIV_EMAIL_DOMAIN_MISMATCH(HttpStatus.BAD_REQUEST, "재학생 메일의 도메인이 맞지 않습니다."),
    MESSAGING_EXCEPTION(HttpStatus.BAD_REQUEST, "수신자 이메일이 올바르지 않습니다."),

    VERIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "재학생 인증 코드가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
