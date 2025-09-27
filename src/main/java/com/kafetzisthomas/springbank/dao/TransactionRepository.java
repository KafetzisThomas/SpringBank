package com.kafetzisthomas.springbank.dao;

import com.kafetzisthomas.springbank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // retrieve a list of transactions associated with the specified owner's email
    List<Transaction> findAllByOwnerEmail(String ownerEmail);

    // retrieve transactions for the given email with timestamp in the inclusive range (start,end)
    List<Transaction> findAllByOwnerEmailAndTimestampBetween(String email, LocalDateTime start, LocalDateTime end);

    // find most recent transaction for the given owner by timestamp for balance calculations
    Optional<Transaction> findTopByOwnerEmailOrderByTimestampDesc(String ownerEmail);

}
