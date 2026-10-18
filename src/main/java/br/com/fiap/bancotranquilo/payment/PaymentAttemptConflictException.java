package br.com.fiap.bancotranquilo.payment;

public class PaymentAttemptConflictException extends RuntimeException {

    public PaymentAttemptConflictException() {
        super("paymentAttemptId ja processado com dados diferentes.");
    }
}
