package com.kafetzisthomas.banking.bankingsystem.dao;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
