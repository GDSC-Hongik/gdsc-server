package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.response.MemberAccountInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberDepartmentResponse;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonMemberService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;
    private final GithubClient githubClient;

    @Transactional(readOnly = true)
    public List<MemberDepartmentResponse> getDepartments() {
        return Arrays.stream(Department.values())
                .map(MemberDepartmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberDepartmentResponse> searchDepartments(String departmentName) {
        if (departmentName == null) {
            return getDepartments();
        }

        return Arrays.stream(Department.values())
                .filter(department -> department.getDepartmentName().contains(departmentName))
                .map(MemberDepartmentResponse::from)
                .toList();
    }

    /**
     * 이벤트 핸들러에서 사용되므로, `@Transactional` 을 사용하지 않습니다.
     */
    public void advanceMemberToRegularByMembership(Long membershipId) {
        Membership membership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new CustomException(MEMBERSHIP_NOT_FOUND));

        Member member = memberRepository
                .findById(membership.getMember().getId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if (membership.isRegularRequirementAllSatisfied()) {
            member.advanceToRegular();
            memberRepository.save(member);

            log.info("[CommonMemberService] 정회원 승급 완료: memberId={}", member.getId());
        }
    }

    /**
     * 이벤트 핸들러에서 사용되므로, `@Transactional` 을 사용하지 않습니다.
     */
    public void demoteMemberToAssociateByMembership(Long membershipId) {
        Membership membership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new CustomException(MEMBERSHIP_NOT_FOUND));

        Member member = memberRepository
                .findById(membership.getMember().getId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        member.demoteToAssociate();

        memberRepository.save(member);

        log.info("[CommonMemberService] 준회원 강등 완료: memberId={}", member.getId());
    }

    @Transactional(readOnly = true)
    public MemberAccountInfoResponse getAccountInfo() {
        Member currentMember = memberUtil.getCurrentMember();
        String githubHandle = githubClient.getGithubHandle(currentMember.getOauthId());
        return MemberAccountInfoResponse.of(currentMember, githubHandle);
    }
}
