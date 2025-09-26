package com.kafetzisthomas.banking.bankingsystem.service;

import com.kafetzisthomas.banking.bankingsystem.dao.TransactionRepository;
import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private BigDecimal getCurrentBalance(String email) {
        return transactionRepository.findTopByOwnerEmailOrderByTimestampDesc(email)
                .map(Transaction::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public void deposit(Transaction transaction, String email) {
        transaction.setOwnerEmail(email);
        transaction.setType("Deposit");
        transaction.setTimestamp(LocalDateTime.now());

        BigDecimal current = getCurrentBalance(email);
        BigDecimal newBalance = current.add(transaction.getAmount());
        transaction.setBalance(newBalance);

        transactionRepository.save(transaction);
    }

    @Override
    public void withdraw(Transaction transaction, String email) {
        transaction.setOwnerEmail(email);
        transaction.setType("Withdraw");
        transaction.setTimestamp(LocalDateTime.now());

        BigDecimal current = getCurrentBalance(email);
        BigDecimal amount = transaction.getAmount();

        if (current.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        BigDecimal newBalance = current.subtract(amount);
        transaction.setBalance(newBalance);

        transactionRepository.save(transaction);
    }

}
