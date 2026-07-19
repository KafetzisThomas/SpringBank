package com.kafetzisthomas.springbank.service;

import com.kafetzisthomas.springbank.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions(String email);

    List<Transaction> getTransactionsByDateRange(String email, java.time.LocalDate start, java.time.LocalDate end);

    void deposit(Transaction transaction, String email);

    void withdraw(Transaction transaction, String email);

}
