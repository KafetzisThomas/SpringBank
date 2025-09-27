package com.kafetzisthomas.springbank.service;

import com.kafetzisthomas.springbank.dao.TransactionRepository;
import com.kafetzisthomas.springbank.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private TransactionRepository transactionRepository;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    void getAllTransactions_returnListFromRepository() {
        Transaction t1 = new Transaction(BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now(), "user@example.com");
        Transaction t2 = new Transaction(BigDecimal.ONE, BigDecimal.valueOf(11), "Deposit", LocalDateTime.now(), "user@example.com");
        when(transactionRepository.findAllByOwnerEmail("test@example.com")).thenReturn(List.of(t1, t2));

        List<Transaction> result = transactionService.getAllTransactions("test@example.com");

        assertEquals(2, result.size());
        verify(transactionRepository).findAllByOwnerEmail("test@example.com");
    }

    @Test
    void deposit_createTransactionWithUpdatedBalance() {
        when(transactionRepository.findTopByOwnerEmailOrderByTimestampDesc("test@example.com"))
                .thenReturn(Optional.of(new Transaction(BigDecimal.TEN, BigDecimal.TEN, "Deposit",
                        LocalDateTime.now(), "test@example.com")));

        Transaction deposit = new Transaction(BigDecimal.valueOf(5),
                null, null, null, null);

        transactionService.deposit(deposit, "test@example.com");

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Deposit", saved.getType());
        assertEquals("test@example.com", saved.getOwnerEmail());
        assertEquals(BigDecimal.valueOf(15), saved.getBalance());
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void withdraw_decreaseBalanceWhenFundsAvailable() {
        when(transactionRepository.findTopByOwnerEmailOrderByTimestampDesc("test@example.com"))
                .thenReturn(Optional.of(new Transaction(BigDecimal.TEN, BigDecimal.TEN, "Deposit",
                        LocalDateTime.now(), "test@example.com")));

        Transaction withdraw = new Transaction(BigDecimal.valueOf(5),
                null, null, null, null);

        transactionService.withdraw(withdraw, "test@example.com");

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Withdraw", saved.getType());
        assertEquals(BigDecimal.valueOf(5), saved.getBalance());
    }
    @Test
    void withdraw_throwExceptionWhenInsufficientFunds() {
        when(transactionRepository.findTopByOwnerEmailOrderByTimestampDesc("test@example.com"))
                .thenReturn(Optional.of(new Transaction(BigDecimal.TEN, BigDecimal.TEN, "Deposit",
                        LocalDateTime.now(), "test@example.com")));

        Transaction withdraw = new Transaction(BigDecimal.valueOf(20),
                null, null, null, null);

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.withdraw(withdraw, "test@example.com"));

        verify(transactionRepository, never()).save(any());
    }

}
