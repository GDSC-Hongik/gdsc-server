package com.gdschongik.gdsc.domain.order.api;

import com.gdschongik.gdsc.domain.order.application.OrderService;
import com.gdschongik.gdsc.domain.order.dto.request.OrderQueryOption;
import com.gdschongik.gdsc.domain.order.dto.response.OrderAdminResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Order", description = "주문 어드민 API입니다.")
@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 목록 조회하기", description = "주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<OrderAdminResponse>> getOrders(
            @ParameterObject @Valid OrderQueryOption queryOption, @ParameterObject Pageable pageable) {
        var response = orderService.searchOrders(queryOption, pageable);
        return ResponseEntity.ok(response);
    }
}
