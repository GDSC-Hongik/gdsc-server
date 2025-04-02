package com.gdschongik.gdsc.domain.discord.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.discord.domain.DiscordValidator;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyAnnouncementV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.DiscordUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonDiscordService {

    private final MemberRepository memberRepository;
    private final StudyAnnouncementV2Repository studyAnnouncementV2Repository;
    private final DiscordUtil discordUtil;
    private final DiscordValidator discordValidator;

    public String getNicknameByDiscordUsername(String discordUsername) {
        return memberRepository
                .findByDiscordUsername(discordUsername)
                .map(Member::getNickname)
                .orElse(null);
    }

    @Transactional
    public void batchDiscordId(String currentDiscordUsername, RequirementStatus discordStatus) {
        Member currentMember = memberRepository
                .findByDiscordUsername(currentDiscordUsername)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        discordValidator.validateAdminPermission(currentMember);

        List<Member> discordSatisfiedMembers = memberRepository.findAllByDiscordStatus(discordStatus);

        discordSatisfiedMembers.forEach(member -> {
            String discordUsername = member.getDiscordUsername();
            try {
                String discordId = discordUtil.getMemberIdByUsername(discordUsername);
                member.updateDiscordId(discordId);
            } catch (CustomException e) {
                log.info("[CommonDiscordService] 디스코드 id 배치 실패: 사유 = {} memberId = {}", e.getMessage(), member.getId());
            }
        });
    }

    /**
     * 이벤트 핸들러에서 사용되므로, `@Transactional` 을 사용하지 않습니다.
     */
    public void addStudyRoleToMember(String studyDiscordRoleId, String memberDiscordId) {
        discordUtil.addRoleToMemberById(studyDiscordRoleId, memberDiscordId);
    }

    /**
     * 이벤트 핸들러에서 사용되므로, `@Transactional` 을 사용하지 않습니다.
     */
    public void removeStudyRoleFromMember(String studyDiscordRoleId, String memberDiscordId) {
        discordUtil.removeRoleFromMemberById(studyDiscordRoleId, memberDiscordId);
    }

    @Transactional
    public void sendStudyAnnouncement(Long studyAnnouncementId) {
        StudyAnnouncementV2 studyAnnouncement = studyAnnouncementV2Repository
                .findById(studyAnnouncementId)
                .orElseThrow(() -> new CustomException(STUDY_ANNOUNCEMENT_NOT_FOUND));

        discordUtil.sendStudyAnnouncementToChannel(
                studyAnnouncement.getStudy().getDiscordChannelId(),
                studyAnnouncement.getStudy().getTitle(),
                studyAnnouncement.getTitle(),
                studyAnnouncement.getLink(),
                studyAnnouncement.getCreatedAt());

        log.info("[CommonDiscordService] 스터디 공지 전송 완료: studyAnnouncementId = {}", studyAnnouncementId);
    }
}
