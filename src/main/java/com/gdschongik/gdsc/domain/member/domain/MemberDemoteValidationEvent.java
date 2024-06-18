package com.gdschongik.gdsc.domain.member.domain;

import com.gdschongik.gdsc.domain.common.model.SemesterType;

public record MemberDemoteValidationEvent(Integer academicYear, SemesterType semesterType) {}
