package com.kafetzisthomas.springbank.repository;

import com.kafetzisthomas.springbank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByOwnerEmailOrderByTimestampAsc(String ownerEmail);

    List<Transaction> findAllByOwnerEmailAndTimestampBetweenOrderByTimestampAsc(String email, LocalDateTime start, LocalDateTime end);

    Optional<Transaction> findTopByOwnerEmailOrderByTimestampDesc(String ownerEmail);

}
