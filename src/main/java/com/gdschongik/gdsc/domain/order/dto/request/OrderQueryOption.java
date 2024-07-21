package com.gdschongik.gdsc.domain.order.dto.request;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.order.domain.OrderStatus;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public record OrderQueryOption(
        String name,
        @Min(2023) Integer academicYear,
        SemesterType semesterType,
        String studentId,
        OrderStatus status,
        String nanoId,
        String paymentKey,
        LocalDateTime approvedAt) {}
