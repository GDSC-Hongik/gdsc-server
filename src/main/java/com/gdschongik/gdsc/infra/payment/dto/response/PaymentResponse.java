package com.gdschongik.gdsc.infra.payment.dto.response;

import jakarta.annotation.Nullable;
import java.util.List;

public record PaymentResponse(
        String version,
        String paymentKey,
        String type,
        String orderId,
        String orderName,
        String mId,
        String currency,
        String method,
        Long totalAmount,
        Long balanceAmount,
        String status,
        String requestedAt,
        String approvedAt,
        Boolean useEscrow,
        @Nullable String lastTransactionKey,
        Long suppliedAmount,
        Long vat,
        Boolean cultureExpense,
        Long taxFreeAmount,
        Long taxExemtionAmount,
        @Nullable List<PaymentCancelDto> cancels,
        Boolean isPartialCancelable,
        @Nullable CardDto card,
        @Nullable TransferDto transfer,
        @Nullable ReceiptDto receipt,
        @Nullable CheckoutDto checkout,
        @Nullable EasyPayDto easyPay,
        String country,
        @Nullable FailureDto failure,
        @Nullable CashReceiptDto cashReceipt,
        @Nullable List<CashReceiptsDto> cashReceipts) {
    // TODO: 시간, enum 관련 매핑 여부 검토
    public record PaymentCancelDto(
            Long cancelAmount,
            String cancelReason,
            Long taxFreeAmount,
            Long refundableAmount,
            Long easyPayDiscountAmount,
            String canceledAt,
            String transactionKey,
            @Nullable String receiptKey,
            String cancelStatus,
            @Nullable String cancelRequestId) {}

    public record CardDto(
            Long amount,
            String issuerCode,
            @Nullable String acquirerCode,
            String number,
            Integer installmentPlanMonths,
            String approveNo,
            Boolean useCardPoint,
            String cardType,
            String ownerType,
            String acquireStatus,
            Boolean isInterestFree,
            @Nullable String interestPayer) {}

    public record TransferDto(String bankCode, String settlementStatus) {}

    public record ReceiptDto(String url) {}

    public record CheckoutDto(String url) {}

    public record EasyPayDto(String provider, Long amount, Long discountAmount) {}

    public record FailureDto(String code, String message) {}

    public record CashReceiptDto(
            String type,
            String receiptKey,
            String issueNumber,
            String receiptUrl,
            Long amount,
            Long taxFreeAmount,
            Long taxExemptionAmount) {}

    public record CashReceiptsDto(
            String receiptKey,
            String orderId,
            String orderName,
            String type,
            String issueNumber,
            String receiptUrl,
            String businessNumber,
            String transactionType,
            Integer amount,
            Integer taxFreeAmount,
            String issueStatus,
            Object failure,
            String customerIdentityNumber,
            String requestedAt) {}
}
