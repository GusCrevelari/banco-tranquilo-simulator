package br.com.fiap.bancotranquilo.payment;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        List<String> details) {
}
