package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.dto.response.MentorStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentorStudyService {

    private final MemberUtil memberUtil;
    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;

    @Transactional(readOnly = true)
    public List<MentorStudyResponse> getStudiesInCharge() {
        Member currentMember = memberUtil.getCurrentMember();
        List<Study> myStudies = studyRepository.findAllByMentor(currentMember);
        return myStudies.stream().map(MentorStudyResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyStudentResponse> getStudyStudents(Long studyId) {
        List<StudyHistory> studyHistories = studyHistoryRepository.findByStudyId(studyId);

        return studyHistories.stream().map(StudyStudentResponse::from).toList();
    }
}
