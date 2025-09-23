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
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
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

    @Override
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existing = transactionRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setAmount(updatedTransaction.getAmount());
        existing.setType(updatedTransaction.getType());
        return transactionRepository.save(existing);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
