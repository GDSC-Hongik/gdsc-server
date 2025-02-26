package com.gdschongik.gdsc.domain.studyv2.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;

public interface AttendanceV2Repository extends JpaRepository<AttendanceV2, Long> {

    boolean existsByStudentAndStudySession(Member student, StudySessionV2 studySession);
}
