package com.kafetzisthomas.banking.bankingsystem.dao;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // retrieve a list of transactions associated with the specified owner's email
    List<Transaction> findAllByOwnerEmail(String ownerEmail);

    // find most recent transaction for the given owner by timestamp for balance calculations
    Optional<Transaction> findTopByOwnerEmailOrderByTimestampDesc(String ownerEmail);

}
