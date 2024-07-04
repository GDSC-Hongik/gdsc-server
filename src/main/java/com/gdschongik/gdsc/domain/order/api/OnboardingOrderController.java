package com.gdschongik.gdsc.domain.order.api;

import com.gdschongik.gdsc.domain.order.application.OrderService;
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

@Tag(name = "Onboarding Order", description = "주문 온보딩 API입니다.")
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
}
