package com.kafetzisthomas.banking.bankingsystem.rest;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import com.kafetzisthomas.banking.bankingsystem.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        mockPrincipal = () -> "test@example.com";
    }

    @Test
    void listTransactions() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setBalance(new BigDecimal("100.00"));
        when(transactionService.getAllTransactions("test@example.com"))
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions/transaction-report"))
                .andExpect(model().attributeExists("transactions", "currentBalance"));
    }

    @Test
    void saveDepositSuccessful() throws Exception {
        Transaction deposit = new Transaction();
        deposit.setAmount(new BigDecimal("200.00"));

        mockMvc.perform(post("/deposit/save")
                        .flashAttr("transaction", deposit)
                        .principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    void saveWithdrawInsufficientFunds() throws Exception {
        Transaction withdrawal = new Transaction();
        withdrawal.setAmount(new BigDecimal("500.00"));

        Mockito.doThrow(new IllegalArgumentException("Insufficient funds"))
                .when(transactionService).withdraw(withdrawal, "test@example.com");

        mockMvc.perform(post("/withdraw/save")
                        .flashAttr("transaction", withdrawal)
                        .principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("errorMessage", "Withdraw failed: Insufficient funds"));
    }

}
