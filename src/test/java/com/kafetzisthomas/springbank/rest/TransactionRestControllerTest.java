package com.kafetzisthomas.springbank.rest;

import com.kafetzisthomas.springbank.entity.Transaction;
import com.kafetzisthomas.springbank.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private static final String EMAIL = "user@test.com";
    private static final Principal PRINCIPAL = () -> EMAIL;

    @Test
    void getTransactions_noDateRange_returnsAll() throws Exception {
        Transaction transaction = new Transaction(BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now(), EMAIL);
        when(transactionService.getAllTransactions(EMAIL)).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions")
                        .principal(PRINCIPAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("Deposit"));

        verify(transactionService).getAllTransactions(EMAIL);
        verify(transactionService, never()).getTransactionsByDateRange(any(), any(String.class));
    }

    @Test
    void getTransactions_withDateRange_filtersResults() throws Exception {
        String daterange = "2026-01-01 - 2026-01-31";
        when(transactionService.getTransactionsByDateRange(EMAIL, daterange)).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions")
                        .param("daterange", daterange)
                        .principal(PRINCIPAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(transactionService).getTransactionsByDateRange(EMAIL, daterange);
        verify(transactionService, never()).getAllTransactions(any());
    }

    @Test
    void getTransactions_blankDateRange_returnsAll() throws Exception {
        when(transactionService.getAllTransactions(EMAIL)).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions")
                        .param("daterange", "   ")
                        .principal(PRINCIPAL))
                .andExpect(status().isOk());

        verify(transactionService).getAllTransactions(EMAIL);
    }

    @Test
    void deposit_success_returns200() throws Exception {
        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 50.00}")
                        .principal(PRINCIPAL))
                .andExpect(status().isOk());

        verify(transactionService).deposit(any(Transaction.class), eq(EMAIL));
    }

    @Test
    void deposit_failure_returns400() throws Exception {
        doThrow(new IllegalArgumentException("Invalid amount"))
                .when(transactionService).deposit(any(Transaction.class), eq(EMAIL));

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": -1}")
                        .principal(PRINCIPAL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_success_returns200() throws Exception {
        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 25.00}")
                        .principal(PRINCIPAL))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdraw successful"));

        verify(transactionService).withdraw(any(Transaction.class), eq(EMAIL));
    }

    @Test
    void withdraw_insufficientFunds_returns400() throws Exception {
        doThrow(new IllegalArgumentException("Insufficient funds"))
                .when(transactionService).withdraw(any(Transaction.class), eq(EMAIL));

        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 9999}")
                        .principal(PRINCIPAL))
                .andExpect(status().isBadRequest());
    }

}
