package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Attendance;
import java.util.List;

public interface AttendanceCustomRepository {
    List<Attendance> findByMemberAndStudyId(Member member, Long studyId);

    void deleteByStudyIdAndMemberId(Long studyId, Long memberId);
}
