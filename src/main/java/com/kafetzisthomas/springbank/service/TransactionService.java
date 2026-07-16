package com.kafetzisthomas.springbank.service;

import com.kafetzisthomas.springbank.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions(String email);

    List<Transaction> getTransactionsByDateRange(String email, LocalDateTime start, LocalDateTime end);

    List<Transaction> getTransactionsByDateRange(String email, String daterange);

    void deposit(Transaction transaction, String email);

    void withdraw(Transaction transaction, String email);

}
