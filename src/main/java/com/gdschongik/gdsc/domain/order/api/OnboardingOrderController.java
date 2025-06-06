package com.gdschongik.gdsc.domain.order.api;

import com.gdschongik.gdsc.domain.order.application.OrderService;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCompleteRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order - Onboarding", description = "주문 온보딩 API입니다.")
@RestController
@RequestMapping("/onboarding/orders")
@RequiredArgsConstructor
public class OnboardingOrderController {

    private final OrderService orderService;

    @Operation(summary = "임시 주문 생성", description = "임시 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createPendingOrder(@Valid @RequestBody OrderCreateRequest request) {
        orderService.createPendingOrder(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "주문 완료", description = "임시 주문을 완료합니다. 요청된 결제는 승인됩니다.")
    @PostMapping("/complete")
    public ResponseEntity<Void> completeOrder(@Valid @RequestBody OrderCompleteRequest request) {
        orderService.completeOrder(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "무료 주문 생성", description = "무료 주문을 생성합니다. 무료 주문은 완료된 상태로 생성됩니다.")
    @PostMapping("/free")
    public ResponseEntity<Void> createFreeOrder(@Valid @RequestBody OrderCreateRequest request) {
        orderService.createFreeOrder(request);
        return ResponseEntity.ok().build();
    }
}
