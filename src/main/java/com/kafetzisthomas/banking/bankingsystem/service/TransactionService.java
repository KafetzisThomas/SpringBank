package com.kafetzisthomas.banking.bankingsystem.service;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;

import java.util.List;

public interface TransactionService {

    // return transactions only for the specified username
    List<Transaction> getAllTransactions(String username);

    // create a new deposit transaction for the specified username
    void deposit(Transaction transaction, String username);

    // create a new withdraw transaction for the specified username
    void withdraw(Transaction transaction, String username);

}
