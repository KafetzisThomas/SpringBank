package com.kafetzisthomas.banking.bankingsystem.service;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

}
