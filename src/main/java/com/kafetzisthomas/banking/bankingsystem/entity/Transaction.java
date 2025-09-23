package com.kafetzisthomas.banking.bankingsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
public class Transaction {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column
    private BigDecimal amount;

    @Column
    private String type;  // deposit or withdraw

    @Column
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String ownerUsername;

    // define constructors

    public Transaction() {

    }

    public Transaction(BigDecimal amount, String type, LocalDateTime timestamp, String ownerUsername) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.ownerUsername = ownerUsername;
    }

    // define getters/setters

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // define toString method

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", ownerUsername='" + ownerUsername + '\'' +
                '}';
    }

}
