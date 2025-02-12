package com.gdschongik.gdsc.domain.studyv2.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyManagerResponse;
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
    private final StudyV2Repository studyV2Repository;

    @Transactional(readOnly = true)
    public List<StudyManagerResponse> getStudiesInCharge() {
        Member mentor = memberUtil.getCurrentMember();
        List<StudyV2> myStudies = studyV2Repository.findAllByMentor(mentor);
        return myStudies.stream().map(StudyManagerResponse::from).toList();
    }
}
