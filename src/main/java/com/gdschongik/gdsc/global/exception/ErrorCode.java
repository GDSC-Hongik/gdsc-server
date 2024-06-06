package com.gdschongik.gdsc.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러가 발생했습니다. 관리자에게 문의 바랍니다."),
    METHOD_ARGUMENT_NULL(HttpStatus.BAD_REQUEST, "인자는 null이 될 수 없습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "인자가 유효하지 않습니다."),
    REGEX_VIOLATION(HttpStatus.BAD_REQUEST, "정규표현식을 위반했습니다."),

    // Auth
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    AUTH_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보가 존재하지 않습니다."),
    AUTH_NOT_PARSABLE(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보 파싱에 실패했습니다."),
    BASE_URI_COOKIE_NOT_FOUND(HttpStatus.NOT_FOUND, "Base URI 쿠키가 존재하지 않습니다."),
    NOT_ALLOWED_BASE_URI(HttpStatus.FORBIDDEN, "허용되지 않은 Base URI입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_ROLE(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // Parameter
    INVALID_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 쿼리 파라미터입니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 커뮤니티 멤버입니다."),
    MEMBER_DELETED(HttpStatus.CONFLICT, "탈퇴한 회원입니다."),
    MEMBER_FORBIDDEN(HttpStatus.CONFLICT, "차단된 회원입니다."),
    MEMBER_ALREADY_GRANTED(HttpStatus.CONFLICT, "이미 승인된 회원입니다."),
    MEMBER_ALREADY_VERIFIED(HttpStatus.CONFLICT, "이미 인증된 상태입니다."),
    MEMBER_DISCORD_USERNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 등록된 디스코드 유저네임입니다."),
    MEMBER_NICKNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    MEMBER_NOT_APPLIED(HttpStatus.CONFLICT, "가입신청서를 제출하지 않은 회원입니다."),
    MEMBER_NOT_GRANTED(HttpStatus.CONFLICT, "승인되지 않은 회원입니다."),

    // Requirement
    UNIV_NOT_VERIFIED(HttpStatus.CONFLICT, "재학생 인증이 완료되지 않았습니다."),
    DISCORD_NOT_VERIFIED(HttpStatus.CONFLICT, "디스코드 인증이 완료되지 않았습니다."),
    BEVY_NOT_VERIFIED(HttpStatus.CONFLICT, "GDSC Bevy 가입이 완료되지 않았습니다."),
    BASIC_INFO_NOT_VERIFIED(HttpStatus.CONFLICT, "기본 회원정보 작성이 완료되지 않았습니다.");

    // Univ Email Verification
    UNIV_EMAIL_ALREADY_VERIFIED(HttpStatus.CONFLICT, "이미 가입된 재학생 메일입니다."),
    UNIV_EMAIL_FORMAT_MISMATCH(HttpStatus.BAD_REQUEST, "형식에 맞지 않는 재학생 메일입니다."),
    UNIV_EMAIL_DOMAIN_MISMATCH(HttpStatus.BAD_REQUEST, "재학생 메일의 도메인이 맞지 않습니다."),
    MESSAGING_EXCEPTION(HttpStatus.BAD_REQUEST, "수신자 이메일이 올바르지 않습니다."),
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "재학생 인증 코드가 존재하지 않습니다."),

    // Discord
    DISCORD_INVALID_CODE_RANGE(HttpStatus.INTERNAL_SERVER_ERROR, "디스코드 인증코드는 4자리 숫자여야 합니다."),
    DISCORD_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저네임으로 발급된 디스코드 인증코드가 존재하지 않습니다."),
    DISCORD_CODE_MISMATCH(HttpStatus.CONFLICT, "디스코드 인증코드가 일치하지 않습니다."),
    DISCORD_ROLE_UNASSIGNABLE(HttpStatus.INTERNAL_SERVER_ERROR, "디스코드 역할 부여가 불가능합니다. 가입 조건을 확인해주세요."),
    DISCORD_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "디스코드 역할을 찾을 수 없습니다."),
    DISCORD_NOT_SIGNUP(HttpStatus.INTERNAL_SERVER_ERROR, "아직 가입신청서를 작성하지 않은 회원입니다."),
    DISCORD_NICKNAME_NOTNULL(HttpStatus.INTERNAL_SERVER_ERROR, "닉네임은 빈 값이 될 수 없습니다."),
    DISCORD_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "디스코드 멤버를 찾을 수 없습니다."),

    // Membership
    PAYMENT_NOT_VERIFIED(HttpStatus.CONFLICT, "회비 납부가 완료되지 않았습니다."),
    MEMBERSHIP_NOT_APPLICABLE(HttpStatus.CONFLICT, "멤버십 가입을 신청할 수 없는 회원입니다."),
    MEMBERSHIP_ALREADY_APPLIED(HttpStatus.CONFLICT, "이미 이번 학기에 멤버십 가입을 신청한 회원입니다."),

    // Recruitment
    DATE_PRECEDENCE_INVALID(HttpStatus.BAD_REQUEST, "종료일이 시작일과 같거나 앞설 수 없습니다."),
    RECRUITMENT_NOT_OPEN(HttpStatus.CONFLICT, "리크루팅 모집기간이 아닙니다."),
    RECRUITMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "열려있는 리크루팅이 없습니다."),
    RECRUITMENT_PERIOD_OVERLAP(HttpStatus.BAD_REQUEST, "모집 기간이 중복됩니다."),
    RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR(HttpStatus.BAD_REQUEST, "모집 시작일과 종료일의 연도가 학년도와 일치하지 않습니다."),
    RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE(HttpStatus.BAD_REQUEST, "모집 시작일과 종료일의 입력된 학기가 일치하지 않습니다."),
    RECRUITMENT_PERIOD_SEMESTER_TYPE_UNMAPPED(HttpStatus.CONFLICT, "모집 시작일과 종료일이 매핑되는 학기가 없습니다."),
    RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS(HttpStatus.BAD_REQUEST, "모집 시작일과 종료일이 학기 시작일로부터 2주 이내에 있지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
