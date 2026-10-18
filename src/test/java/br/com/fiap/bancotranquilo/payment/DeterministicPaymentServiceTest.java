package br.com.fiap.bancotranquilo.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeterministicPaymentServiceTest {

    private final DeterministicPaymentService service = new DeterministicPaymentService();

    @Test
    void shouldApproveOddPurchaseAndReturnSameResultForDuplicateAttempt() {
        BankPaymentRequest request = request(11L);

        BankPaymentResponse first = service.authorize(request);
        BankPaymentResponse duplicate = service.authorize(request);

        assertThat(first.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(first.authorizationCode()).startsWith("BT-");
        assertThat(duplicate).isEqualTo(first);
    }

    @Test
    void shouldRejectEvenPurchase() {
        BankPaymentResponse response = service.authorize(request(12L));

        assertThat(response.status()).isEqualTo(PaymentStatus.REJECTED);
        assertThat(response.reason()).isEqualTo("PURCHASE_ID_EVEN");
        assertThat(response.authorizationCode()).isNull();
    }

    @Test
    void shouldRejectConflictingReuseOfAttemptId() {
        UUID attemptId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        service.authorize(new BankPaymentRequest(
                attemptId, 11L, correlationId, new BigDecimal("10.00"), "BRL"));

        assertThatThrownBy(() -> service.authorize(new BankPaymentRequest(
                attemptId, 11L, correlationId, new BigDecimal("11.00"), "BRL")))
                .isInstanceOf(PaymentAttemptConflictException.class);
    }

    private BankPaymentRequest request(long purchaseId) {
        return new BankPaymentRequest(
                UUID.randomUUID(),
                purchaseId,
                UUID.randomUUID(),
                new BigDecimal("39.90"),
                "BRL");
    }
}
