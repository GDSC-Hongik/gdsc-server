package com.gdschongik.gdsc.domain.email.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.EMAIL_NOT_SENT;
import static com.gdschongik.gdsc.global.exception.ErrorCode.EXPIRED_EMAIL_VERIFICATION_TOKEN;

import com.gdschongik.gdsc.domain.email.dao.UnivEmailVerificationRepository;
import com.gdschongik.gdsc.domain.email.domain.HongikUnivEmailValidator;
import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import com.gdschongik.gdsc.domain.email.dto.request.EmailVerificationTokenDto;
import com.gdschongik.gdsc.domain.email.dto.request.UnivEmailVerificationRequest;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.email.EmailVerificationTokenUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnivEmailVerificationService {

    private final EmailVerificationTokenUtil emailVerificationTokenUtil;
    private final MemberRepository memberRepository;
    private final UnivEmailVerificationRepository univEmailVerificationRepository;

    private final HongikUnivEmailValidator hongikUnivEmailValidator;

    @Transactional
    public void verifyMemberUnivEmail(UnivEmailVerificationRequest request) {
        EmailVerificationTokenDto emailVerificationToken = getEmailVerificationToken(request.token());
        Member member = getMemberById(emailVerificationToken.memberId());
        member.completeUnivEmailVerification(emailVerificationToken.email());
    }

    public Optional<UnivEmailVerification> getUnivEmailVerificationFromRedis(Long memberId) {
        return univEmailVerificationRepository.findById(memberId);
    }

    private EmailVerificationTokenDto getEmailVerificationToken(String verificationToken) {
        EmailVerificationTokenDto emailVerificationTokenDto =
                emailVerificationTokenUtil.parseEmailVerificationTokenDto(verificationToken);
        final Optional<UnivEmailVerification> univEmailVerification =
                getUnivEmailVerificationFromRedis(emailVerificationTokenDto.memberId());

        validateUnivEmailVerification(univEmailVerification, verificationToken);

        return emailVerificationTokenDto;
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // redis 안의 존재하는 메일인증 정보로 검증
    private void validateUnivEmailVerification(
            Optional<UnivEmailVerification> optionalUnivEmailVerification, String currentToken) {
        // 토큰이 비었는데 인증하려할 시 에러 (인증메일을 보내지 않았거나, 만료된 경우)
        if (optionalUnivEmailVerification.isEmpty()) {
            throw new CustomException(EMAIL_NOT_SENT);
        }
        // 토큰이 redis에 저장된 토큰과 다르면 만료되었다는 에러 (메일 여러번 보낸 경우)
        else if (!optionalUnivEmailVerification.get().getVerificationToken().equals(currentToken)) {
            throw new CustomException(EXPIRED_EMAIL_VERIFICATION_TOKEN);
        }
    }
}
