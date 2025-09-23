package com.kafetzisthomas.banking.bankingsystem.service;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id);

    void deposit(Transaction transaction);

    void withdraw(Transaction transaction);

    Transaction updateTransaction(Long id, Transaction updatedTransaction);

    void deleteTransaction(Long id);

}
