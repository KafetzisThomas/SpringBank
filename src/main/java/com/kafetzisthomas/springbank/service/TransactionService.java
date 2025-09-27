package com.kafetzisthomas.springbank.service;

import com.kafetzisthomas.springbank.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    // return transactions only for the specified email
    List<Transaction> getAllTransactions(String email);

    // retrieve transactions for a user within the inclusive time window (start,end)
    List<Transaction> getTransactionsByDateRange(String email, LocalDateTime start, LocalDateTime end);

    // create a new deposit transaction for the specified email
    void deposit(Transaction transaction, String email);

    // create a new withdraw transaction for the specified email
    void withdraw(Transaction transaction, String email);

}
