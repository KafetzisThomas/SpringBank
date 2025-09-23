package com.kafetzisthomas.banking.bankingsystem.service;

import com.kafetzisthomas.banking.bankingsystem.dao.TransactionRepository;
import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public void deposit(Transaction transaction) {
        transaction.setType("Deposit");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public void withdraw(Transaction transaction) {
        transaction.setType("Withdraw");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

}
