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
    public List<Transaction> getAllTransactions(String email) {
        return transactionRepository.findAllByOwnerEmail(email);
    }

    @Override
    public void deposit(Transaction transaction, String email) {
        transaction.setOwnerEmail(email);
        transaction.setType("Deposit");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public void withdraw(Transaction transaction, String email) {
        transaction.setOwnerEmail(email);
        transaction.setType("Withdraw");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

}
