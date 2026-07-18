package com.kafetzisthomas.springbank.service;

import com.kafetzisthomas.springbank.entity.Transaction;
import com.kafetzisthomas.springbank.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private static final String EMAIL = "test@test.com";

    private TransactionRepository transactionRepository;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    private Transaction transactionWithAmount(BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        return transaction;
    }

    private void stubCurrentBalance(BigDecimal balance) {
        Transaction latest = new Transaction();
        latest.setBalance(balance);
        when(transactionRepository.findTopByOwnerEmailOrderByTimestampDesc(EMAIL)).thenReturn(Optional.of(latest));
    }

    @Test
    void getAllTransactions_returnList() {
        Transaction transaction1 = new Transaction(BigDecimal.TEN, BigDecimal.TEN, "Deposit", LocalDateTime.now(), EMAIL);
        Transaction transaction2 = new Transaction(BigDecimal.ONE, BigDecimal.valueOf(11), "Deposit", LocalDateTime.now(), EMAIL);
        when(transactionRepository.findAllByOwnerEmailOrderByTimestampAsc(EMAIL)).thenReturn(List.of(transaction1, transaction2));

        List<Transaction> result = transactionService.getAllTransactions(EMAIL);

        assertEquals(2, result.size());
        verify(transactionRepository).findAllByOwnerEmailOrderByTimestampAsc(EMAIL);
    }

    @Test
    void getTransactionsByDateRange_callsRepository() {
        LocalDateTime start = LocalDate.of(2026, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(2026, 1, 31).atTime(23, 59, 59);

        transactionService.getTransactionsByDateRange(EMAIL, start, end);

        verify(transactionRepository).findAllByOwnerEmailAndTimestampBetweenOrderByTimestampAsc(EMAIL, start, end);
    }

    @Test
    void getTransactionsByDateRange_parsesValidStrings() {
        transactionService.getTransactionsByDateRange(EMAIL, "2026-01-01 - 2026-01-31");

        verify(transactionRepository).findAllByOwnerEmailAndTimestampBetweenOrderByTimestampAsc(
                eq(EMAIL),
                eq(LocalDate.of(2026, 1, 1).atStartOfDay()),
                eq(LocalDate.of(2026, 1, 31).atTime(23, 59, 59))
        );
    }

    @Test
    void getTransactionsByDateRange_nullString_fallsBackToAll() {
        transactionService.getTransactionsByDateRange(EMAIL, (String) null);

        verify(transactionRepository).findAllByOwnerEmailOrderByTimestampAsc(EMAIL);
        verify(transactionRepository, never()).findAllByOwnerEmailAndTimestampBetweenOrderByTimestampAsc(any(), any(), any());
    }

    @Test
    void getTransactionsByDateRange_noSeparator_fallsBackToAll() {
        transactionService.getTransactionsByDateRange(EMAIL, "2026-01-01");

        verify(transactionRepository).findAllByOwnerEmailOrderByTimestampAsc(EMAIL);
    }

    @Test
    void getTransactionsByDateRange_malformedDate_fallsBackToAll() {
        transactionService.getTransactionsByDateRange(EMAIL, "te-s-t - te-s-t");

        verify(transactionRepository).findAllByOwnerEmailOrderByTimestampAsc(EMAIL);
    }

    @Test
    void deposit_firstDeposit_balanceEqualsAmount() {
        when(transactionRepository.findTopByOwnerEmailOrderByTimestampDesc(EMAIL)).thenReturn(Optional.empty());

        Transaction deposit = transactionWithAmount(BigDecimal.valueOf(100));
        transactionService.deposit(deposit, EMAIL);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Deposit", saved.getType());
        assertEquals(EMAIL, saved.getOwnerEmail());
        assertEquals(0, BigDecimal.valueOf(100).compareTo(saved.getBalance()));
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void deposit_existingBalance_addsToBalance() {
        stubCurrentBalance(BigDecimal.TEN);

        Transaction deposit = transactionWithAmount(BigDecimal.valueOf(5));
        transactionService.deposit(deposit, EMAIL);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Deposit", saved.getType());
        assertEquals(EMAIL, saved.getOwnerEmail());
        assertEquals(0, BigDecimal.valueOf(15).compareTo(saved.getBalance()));
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void withdraw_sufficientFunds_decreasesBalance() {
        stubCurrentBalance(BigDecimal.TEN);

        Transaction withdraw = transactionWithAmount(BigDecimal.valueOf(5));
        transactionService.withdraw(withdraw, EMAIL);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Withdraw", saved.getType());
        assertEquals(0, BigDecimal.valueOf(5).compareTo(saved.getBalance()));
    }

    @Test
    void withdraw_exactBalance_resultsInZero() {
        stubCurrentBalance(BigDecimal.TEN);

        Transaction withdraw = transactionWithAmount(BigDecimal.TEN);
        transactionService.withdraw(withdraw, EMAIL);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        assertEquals(0, BigDecimal.ZERO.compareTo(captor.getValue().getBalance()));
    }

    @Test
    void withdraw_insufficientFunds_throwsAndDoesNotSave() {
        stubCurrentBalance(BigDecimal.TEN);

        Transaction withdraw = transactionWithAmount(BigDecimal.valueOf(20));

        assertThrows(IllegalArgumentException.class, () -> transactionService.withdraw(withdraw, EMAIL));

        verify(transactionRepository, never()).save(any());
    }

}
