package com.gdschongik.gdsc.domain.member.dto.request;

import com.gdschongik.gdsc.domain.common.model.SemesterType;

public record MemberDemoteRequest(Integer academicYear, SemesterType semesterType) {}
