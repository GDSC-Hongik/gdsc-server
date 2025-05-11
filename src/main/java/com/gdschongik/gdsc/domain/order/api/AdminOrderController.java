package com.gdschongik.gdsc.domain.order.api;

import com.gdschongik.gdsc.domain.order.application.OrderService;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCancelRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderQueryOption;
import com.gdschongik.gdsc.domain.order.dto.response.OrderAdminResponse;
import com.gdschongik.gdsc.infra.feign.payment.dto.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order - Admin", description = "주문 어드민 API입니다.")
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

    @Operation(
            summary = "완료된 유료 주문 결제정보 조회하기",
            description = "주문 결제정보를 조회합니다. 토스페이먼츠 API의 결제 정보인 Payment 객체를 반환합니다. 완료된 유료 주문만 조회할 수 있습니다")
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getCompletedOrderPayment(@PathVariable Long orderId) {
        var response = orderService.getCompletedPaidOrderPayment(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 결제 취소하기", description = "주문 상태를 취소로 변경하고 결제를 취소합니다. 회비납입상태를 대기로 변경하고, 준회원으로 강등합니다.")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId, @Valid @RequestBody OrderCancelRequest request) {
        orderService.cancelOrder(orderId, request);
        return ResponseEntity.noContent().build();
    }
}
