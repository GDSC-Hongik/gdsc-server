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
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

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

    // Money
    MONEY_AMOUNT_NOT_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "금액은 null이 될 수 없습니다."),

    // Period
    PERIOD_OVERLAP(HttpStatus.CONFLICT, "기간이 중복됩니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 커뮤니티 멤버입니다."),
    MEMBER_DELETED(HttpStatus.CONFLICT, "탈퇴한 회원입니다."),
    MEMBER_FORBIDDEN(HttpStatus.CONFLICT, "차단된 회원입니다."),
    MEMBER_ALREADY_ASSOCIATE(HttpStatus.CONFLICT, "이미 준회원 역할에 해당하는 회원입니다."),
    MEMBER_ALREADY_REGULAR(HttpStatus.CONFLICT, "이미 정회원 역할에 해당하는 회원입니다."),
    MEMBER_DISCORD_USERNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 등록된 디스코드 유저네임입니다."),
    MEMBER_NICKNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    MEMBER_NOT_APPLIED(HttpStatus.CONFLICT, "가입신청서를 제출하지 않은 회원입니다."),
    MEMBER_NOT_ASSOCIATE(HttpStatus.CONFLICT, "준회원이 아닌 회원입니다."),

    // Requirement
    UNIV_NOT_SATISFIED(HttpStatus.CONFLICT, "재학생 인증이 완료되지 않았습니다."),
    DISCORD_NOT_SATISFIED(HttpStatus.CONFLICT, "디스코드 인증이 완료되지 않았습니다."),
    BEVY_NOT_SATISFIED(HttpStatus.CONFLICT, "GDSC Bevy 가입이 완료되지 않았습니다."),
    EMAIL_ALREADY_SATISFIED(HttpStatus.CONFLICT, "이미 이메일 인증된 회원입니다."),
    BASIC_INFO_NOT_SATISFIED(HttpStatus.CONFLICT, "기본 회원정보 작성이 완료되지 않았습니다."),

    // Univ Email Verification
    UNIV_EMAIL_ALREADY_SATISFIED(HttpStatus.CONFLICT, "이미 가입된 재학생 메일입니다."),
    UNIV_EMAIL_FORMAT_MISMATCH(HttpStatus.BAD_REQUEST, "형식에 맞지 않는 재학생 메일입니다."),
    UNIV_EMAIL_DOMAIN_MISMATCH(HttpStatus.BAD_REQUEST, "재학생 메일의 도메인이 맞지 않습니다."),
    MESSAGING_EXCEPTION(HttpStatus.BAD_REQUEST, "수신자 이메일이 올바르지 않습니다."),
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "재학생 인증 코드가 존재하지 않습니다."),
    EXPIRED_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "이메일 인증 토큰이 만료되었습니다."),
    INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 이메일 인증 토큰입니다."),

    // Discord
    DISCORD_INVALID_CODE_RANGE(HttpStatus.INTERNAL_SERVER_ERROR, "디스코드 인증코드는 4자리 숫자여야 합니다."),
    DISCORD_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저네임으로 발급된 디스코드 인증코드가 존재하지 않습니다."),
    DISCORD_CODE_MISMATCH(HttpStatus.CONFLICT, "디스코드 인증코드가 일치하지 않습니다."),
    DISCORD_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "디스코드 역할을 찾을 수 없습니다."),
    DISCORD_NOT_SIGNUP(HttpStatus.INTERNAL_SERVER_ERROR, "아직 가입신청서를 작성하지 않은 회원입니다."),
    DISCORD_NICKNAME_NOTNULL(HttpStatus.INTERNAL_SERVER_ERROR, "닉네임은 빈 값이 될 수 없습니다."),
    DISCORD_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "디스코드 멤버를 찾을 수 없습니다."),

    // Membership
    PAYMENT_NOT_SATISFIED(HttpStatus.CONFLICT, "회비 납부가 완료되지 않았습니다."),
    MEMBERSHIP_NOT_APPLICABLE(HttpStatus.CONFLICT, "멤버십 가입을 신청할 수 없는 회원입니다."),
    MEMBERSHIP_ALREADY_SUBMITTED(HttpStatus.CONFLICT, "이미 이번 학기에 멤버십 가입을 신청한 회원입니다."),
    MEMBERSHIP_ALREADY_SATISFIED(HttpStatus.CONFLICT, "이미 이번 학기에 정회원 승급을 완료한 회원입니다."),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 멤버십이 존재하지 않습니다."),
    MEMBERSHIP_RECRUITMENT_ROUND_NOT_OPEN(HttpStatus.CONFLICT, "리크루팅 회차 모집기간이 아닙니다."),

    // Recruitment
    DATE_PRECEDENCE_INVALID(HttpStatus.BAD_REQUEST, "종료일이 시작일과 같거나 앞설 수 없습니다."),
    RECRUITMENT_OVERLAP(HttpStatus.BAD_REQUEST, "해당 학기에 이미 리크루팅이 존재합니다."),
    RECRUITMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "리크루팅이 존재하지 않습니다."),
    RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS(HttpStatus.BAD_REQUEST, "모집 시작일과 종료일이 학기 시작일로부터 2주 이내에 있지 않습니다."),

    // RecruitmentRound
    RECRUITMENT_ROUND_NOT_FOUND(HttpStatus.NOT_FOUND, "모집회차가 존재하지 않습니다."),
    RECRUITMENT_ROUND_TYPE_OVERLAP(HttpStatus.BAD_REQUEST, "모집 차수가 중복됩니다."),
    RECRUITMENT_ROUND_STARTDATE_ALREADY_PASSED(HttpStatus.BAD_REQUEST, "이미 모집 시작일이 지난 모집회차입니다."),
    ROUND_ONE_DOES_NOT_EXIST(HttpStatus.CONFLICT, "1차 모집이 존재하지 않습니다."),
    RECRUITMENT_ROUND_OPEN_NOT_FOUND(HttpStatus.NOT_FOUND, "진행중인 모집회차가 존재하지 않습니다."),

    // Coupon
    COUPON_DISCOUNT_AMOUNT_NOT_POSITIVE(HttpStatus.CONFLICT, "쿠폰의 할인 금액은 0보다 커야 합니다."),
    COUPON_NOT_USABLE_ALREADY_USED(HttpStatus.CONFLICT, "이미 사용한 쿠폰은 사용할 수 없습니다."),
    COUPON_NOT_USABLE_REVOKED(HttpStatus.CONFLICT, "회수된 쿠폰은 사용할 수 없습니다."),
    COUPON_NOT_REVOKABLE_ALREADY_REVOKED(HttpStatus.CONFLICT, "이미 회수된 쿠폰은 다시 회수할 수 없습니다."),
    COUPON_NOT_REVOKABLE_ALREADY_USED(HttpStatus.CONFLICT, "이미 사용한 쿠폰은 회수할 수 없습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    ISSUED_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 발급쿠폰입니다."),

    // Study
    STUDY_APPLICATION_START_DATE_INVALID(HttpStatus.CONFLICT, "스터디 신청기간 시작일이 스터디 시작일보다 빠릅니다."),
    STUDY_MENTOR_IS_UNAUTHORIZED(HttpStatus.CONFLICT, "게스트인 회원은 멘토로 지정할 수 없습니다."),
    ON_OFF_LINE_STUDY_TIME_IS_ESSENTIAL(HttpStatus.CONFLICT, "온오프라인 스터디는 스터디 시간이 필요합니다."),
    STUDY_TIME_INVALID(HttpStatus.CONFLICT, "스터디종료 시각이 스터디시작 시각보다 빠릅니다."),
    ASSIGNMENT_STUDY_CAN_NOT_INPUT_STUDY_TIME(HttpStatus.CONFLICT, "과제 스터디는 스터디 시간을 입력할 수 없습니다."),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    ORDER_MEMBERSHIP_MEMBER_MISMATCH(HttpStatus.CONFLICT, "주문 대상 멤버십의 멤버와 현재 로그인한 멤버가 일치하지 않습니다."),
    ORDER_MEMBERSHIP_ALREADY_PAID(HttpStatus.CONFLICT, "주문 대상 멤버십의 회비가 이미 납부되었습니다."),
    ORDER_RECRUITMENT_PERIOD_INVALID(HttpStatus.CONFLICT, "주문 대상 멤버십의 리크루팅의 지원기간이 아닙니다."),
    ORDER_ISSUED_COUPON_MEMBER_MISMATCH(HttpStatus.CONFLICT, "주문 시 사용할 발급쿠폰의 멤버와 현재 로그인한 멤버가 일치하지 않습니다."),
    ORDER_TOTAL_AMOUNT_MISMATCH(HttpStatus.CONFLICT, "주문 금액은 리쿠르팅 회비와 일치해야 합니다."),
    ORDER_DISCOUNT_AMOUNT_NOT_ZERO(HttpStatus.CONFLICT, "쿠폰 미사용시 할인 금액은 0이어야 합니다."),
    ORDER_DISCOUNT_AMOUNT_MISMATCH(HttpStatus.CONFLICT, "쿠폰 사용시 할인 금액은 쿠폰의 할인 금액과 일치해야 합니다."),
    ORDER_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 완료된 주문입니다."),
    ORDER_COMPLETE_AMOUNT_MISMATCH(HttpStatus.CONFLICT, "주문 최종결제금액이 주문완료요청의 결제금액과 일치하지 않습니다."),
    ORDER_COMPLETE_MEMBER_MISMATCH(HttpStatus.CONFLICT, "주문자와 현재 로그인한 멤버가 일치하지 않습니다."),
    ORDER_COMPLETED_PAID_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문이거나, 완료되지 않은 유료 주문입니다."),
    ORDER_CANCEL_NOT_COMPLETED(HttpStatus.CONFLICT, "완료되지 않은 주문은 취소할 수 없습니다."),
    ORDER_CANCEL_FREE_ORDER(HttpStatus.CONFLICT, "무료 주문은 취소할 수 없습니다."),
    ORDER_CANCEL_RESPONSE_NOT_FOUND(
            HttpStatus.INTERNAL_SERVER_ERROR, "주문 결제가 취소되었지만, 응답에 취소 정보가 존재하지 않습니다. 관리자에게 문의 바랍니다."),
    ORDER_FREE_FINAL_PAYMENT_NOT_ZERO(HttpStatus.CONFLICT, "무료 주문의 최종결제금액은 0원이어야 합니다."),

    // Order - MoneyInfo
    ORDER_FINAL_PAYMENT_AMOUNT_MISMATCH(HttpStatus.CONFLICT, "주문 최종결제금액은 주문총액에서 할인금액을 뺀 값이어야 합니다."),

    // Assignment
    ASSIGNMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "과제가 존재하지 않습니다."),
    ASSIGNMENT_CAN_NOT_BE_UPDATED(HttpStatus.CONFLICT, "휴강인 과제는 수정될 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
