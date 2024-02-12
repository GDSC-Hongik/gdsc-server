package com.gdschongik.gdsc.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MemberGrantRequest(@Schema(description = "승인할 멤버 ID 리스트") List<Long> memberIdList) {}
