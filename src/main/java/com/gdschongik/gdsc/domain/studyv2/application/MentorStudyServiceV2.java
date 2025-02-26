package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyUpdateCommand;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyManagerResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyServiceV2 {

    private final MemberUtil memberUtil;
    private final StudyValidatorV2 studyValidatorV2;
    private final StudyV2Repository studyV2Repository;
    private final StudyAnnouncementV2Repository studyAnnouncementV2Repository;

    @Transactional(readOnly = true)
    public List<StudyManagerResponse> getStudiesInCharge() {
        Member mentor = memberUtil.getCurrentMember();
        List<StudyV2> myStudies = studyV2Repository.findAllByMentor(mentor);
        return myStudies.stream().map(StudyManagerResponse::from).toList();
    }

    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study =
                studyV2Repository.findFetchById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        studyValidatorV2.validateStudyMentor(currentMember, study);

        StudyUpdateCommand command = request.toCommand();

        study.update(command);
        studyV2Repository.save(study);

        log.info("[MentoryStudyServiceV2] 스터디 정보 수정 완료: studyId={}", studyId);
    }
}
