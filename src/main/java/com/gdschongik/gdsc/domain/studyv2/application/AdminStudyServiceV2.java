package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceNumberGenerator;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyFactory;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyManagerResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminStudyServiceV2 {

    private final StudyV2Repository studyV2Repository;
    private final MemberRepository memberRepository;
    private final StudyFactory studyFactory;
    private final AttendanceNumberGenerator attendanceNumberGenerator;

    @Transactional
    public void createStudy(StudyCreateRequest request) {
        Member mentor =
                memberRepository.findById(request.mentorId()).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        mentor.assignToMentor();

        StudyV2 study = studyFactory.create(
                request.type(),
                request.title(),
                request.description(),
                request.descriptionNotionLink(),
                request.semester(),
                request.totalRound(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime(),
                request.applicationPeriod(),
                request.discordChannelId(),
                request.discordRoleId(),
                mentor,
                attendanceNumberGenerator);

        memberRepository.save(mentor);
        studyV2Repository.save(study);

        log.info("[AdminStudyServiceV2] 스터디 생성 완료: studyId = {}", study.getId());
    }

    @Transactional(readOnly = true)
    public List<StudyManagerResponse> getAllStudies() {
        return studyV2Repository.findFetchAll().stream()
                .map(StudyManagerResponse::from)
                .toList();
    }

    @Transactional
    public void deleteStudy(Long studyId) {
        studyV2Repository.deleteById(studyId);

        log.info("[AdminStudyServiceV2] 스터디 삭제 완료: studyId = {}", studyId);
    }
}
