package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceV2Repository extends JpaRepository<AttendanceV2, Long>, AttendanceV2CustomRepository {}
