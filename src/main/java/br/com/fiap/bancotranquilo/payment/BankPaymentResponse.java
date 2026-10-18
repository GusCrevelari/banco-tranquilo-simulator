package br.com.fiap.bancotranquilo.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BankPaymentResponse(
        UUID paymentAttemptId,
        Long purchaseId,
        UUID correlationId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        String authorizationCode,
        String reason) {
}
