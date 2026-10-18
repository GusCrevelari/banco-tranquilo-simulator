package br.com.fiap.bancotranquilo.payment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final DeterministicPaymentService paymentService;

    public PaymentController(DeterministicPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<BankPaymentResponse> authorize(@Valid @RequestBody BankPaymentRequest request) {
        return ResponseEntity.ok(paymentService.authorize(request));
    }
}
