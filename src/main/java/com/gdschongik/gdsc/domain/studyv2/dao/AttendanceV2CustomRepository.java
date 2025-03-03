package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.util.List;

public interface AttendanceV2CustomRepository {
    List<AttendanceV2> findFetchByMemberAndStudy(Member member, StudyV2 study);
}
