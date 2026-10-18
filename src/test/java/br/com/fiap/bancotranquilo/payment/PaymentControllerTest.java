package br.com.fiap.bancotranquilo.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PaymentController.class)
@Import({DeterministicPaymentService.class, PaymentExceptionHandler.class})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeApprovedResult() throws Exception {
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "paymentAttemptId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                                  "purchaseId": 11,
                                  "correlationId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                                  "amount": 39.90,
                                  "currency": "BRL"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.authorizationCode").value("BT-AAAAAAAA"));
    }

    @Test
    void shouldExposeRejectedResult() throws Exception {
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "paymentAttemptId": "cccccccc-cccc-cccc-cccc-cccccccccccc",
                                  "purchaseId": 12,
                                  "correlationId": "dddddddd-dddd-dddd-dddd-dddddddddddd",
                                  "amount": 49.90,
                                  "currency": "BRL"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.reason").value("PURCHASE_ID_EVEN"));
    }

    @Test
    void shouldValidateInput() throws Exception {
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }
}
