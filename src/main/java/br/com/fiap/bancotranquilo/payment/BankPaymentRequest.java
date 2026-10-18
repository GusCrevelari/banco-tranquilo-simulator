package br.com.fiap.bancotranquilo.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

public record BankPaymentRequest(
        @NotNull UUID paymentAttemptId,
        @NotNull Long purchaseId,
        @NotNull UUID correlationId,
        @NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal amount,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency) {
}
