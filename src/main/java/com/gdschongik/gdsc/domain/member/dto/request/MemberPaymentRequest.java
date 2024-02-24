package com.gdschongik.gdsc.domain.member.dto.request;

import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberPaymentRequest(@Schema(description = "변경할 상태") RequirementStatus status) {}
