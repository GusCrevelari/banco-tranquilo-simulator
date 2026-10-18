package br.com.fiap.bancotranquilo.payment;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return response(HttpStatus.BAD_REQUEST, details);
    }

    @ExceptionHandler(PaymentAttemptConflictException.class)
    ResponseEntity<ApiError> conflict(PaymentAttemptConflictException exception) {
        return response(HttpStatus.CONFLICT, List.of(exception.getMessage()));
    }

    private ResponseEntity<ApiError> response(HttpStatus status, List<String> details) {
        return ResponseEntity.status(status).body(new ApiError(
                OffsetDateTime.now(ZoneOffset.UTC),
                status.value(),
                status.getReasonPhrase(),
                details));
    }
}
