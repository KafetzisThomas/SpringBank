package com.kafetzisthomas.banking.bankingsystem.service;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;

import java.util.List;

public interface TransactionService {

    // return transactions only for the specified email
    List<Transaction> getAllTransactions(String email);

    // create a new deposit transaction for the specified email
    void deposit(Transaction transaction, String email);

    // create a new withdraw transaction for the specified email
    void withdraw(Transaction transaction, String email);

}
