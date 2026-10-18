package br.com.fiap.bancotranquilo.payment;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class DeterministicPaymentService {

    private final ConcurrentMap<java.util.UUID, ProcessedAttempt> attempts = new ConcurrentHashMap<>();

    public BankPaymentResponse authorize(BankPaymentRequest request) {
        return attempts.compute(request.paymentAttemptId(), (attemptId, current) -> {
            if (current != null) {
                if (!current.request().equals(request)) {
                    throw new PaymentAttemptConflictException();
                }
                return current;
            }
            return new ProcessedAttempt(request, decide(request));
        }).response();
    }

    private BankPaymentResponse decide(BankPaymentRequest request) {
        boolean approved = request.purchaseId() % 2 != 0;
        String suffix = request.paymentAttemptId().toString().substring(0, 8).toUpperCase();
        return new BankPaymentResponse(
                request.paymentAttemptId(),
                request.purchaseId(),
                request.correlationId(),
                request.amount(),
                request.currency(),
                approved ? PaymentStatus.APPROVED : PaymentStatus.REJECTED,
                approved ? "BT-" + suffix : null,
                approved ? null : "PURCHASE_ID_EVEN");
    }

    private record ProcessedAttempt(BankPaymentRequest request, BankPaymentResponse response) {
    }
}
