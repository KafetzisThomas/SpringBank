package com.kafetzisthomas.springbank.controller;

import com.kafetzisthomas.springbank.entity.Transaction;
import com.kafetzisthomas.springbank.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private static final String EMAIL = "user@test.com";
    private static final Principal PRINCIPAL = () -> EMAIL;

    @Test
    void listTransactions_noDates_returnsAllTransactions() throws Exception {
        when(transactionService.getAllTransactions(EMAIL)).thenReturn(List.of());

        mockMvc.perform(get("/")
                        .with(csrf())
                        .principal(PRINCIPAL))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions/transaction-report"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("currentBalance"));

        verify(transactionService).getAllTransactions(EMAIL);
    }

    @Test
    void listTransactions_withDates_returnsFilteredTransactions() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);
        when(transactionService.getTransactionsByDateRange(EMAIL, startDate, endDate)).thenReturn(List.of());

        mockMvc.perform(get("/")
                        .with(csrf())
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-31")
                        .principal(PRINCIPAL))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions/transaction-report"))
                .andExpect(model().attributeExists("transactions"));

        verify(transactionService).getTransactionsByDateRange(EMAIL, startDate, endDate);
    }

    @Test
    void showDepositForm_returnsDepositView() throws Exception {
        mockMvc.perform(get("/deposit")
                        .with(csrf())
                        .principal(PRINCIPAL))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions/deposit-form"))
                .andExpect(model().attributeExists("transaction"));
    }

    @Test
    void saveDeposit_success_redirectsHome() throws Exception {
        mockMvc.perform(post("/deposit/save")
                        .with(csrf())
                        .param("amount", "50")
                        .principal(PRINCIPAL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(transactionService).deposit(any(Transaction.class), eq(EMAIL));
    }

    @Test
    void saveDeposit_failure_redirectsHomeWithError() throws Exception {
        doThrow(new IllegalArgumentException("Invalid amount")).when(transactionService).deposit(any(Transaction.class), eq(EMAIL));

        mockMvc.perform(post("/deposit/save")
                        .with(csrf())
                        .param("amount", "-5")
                        .principal(PRINCIPAL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    void saveWithdraw_success_redirectsHome() throws Exception {
        mockMvc.perform(post("/withdraw/save")
                        .with(csrf())
                        .param("amount", "25")
                        .principal(PRINCIPAL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(transactionService).withdraw(any(Transaction.class), eq(EMAIL));
    }

    @Test
    void saveWithdraw_failure_redirectsHomeWithError() throws Exception {
        doThrow(new IllegalArgumentException("Insufficient funds")).when(transactionService).withdraw(any(Transaction.class), eq(EMAIL));

        mockMvc.perform(post("/withdraw/save")
                        .with(csrf())
                        .param("amount", "9999")
                        .principal(PRINCIPAL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
