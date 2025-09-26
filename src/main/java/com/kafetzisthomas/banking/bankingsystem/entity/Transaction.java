package com.kafetzisthomas.banking.bankingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

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

    @Column(nullable = false)
    @NotNull(message="is required")
    @DecimalMin(value="0.01", message="must be greater than zero")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotNull(message="is required")
    @DecimalMin(value="0.01", message="must be greater than zero")
    private BigDecimal balance;

    @Column(nullable = false)
    @NotNull(message="is required")
    private String type;  // deposit or withdraw

    @Column(nullable = false)
    @NotNull(message="is required")
    private LocalDateTime timestamp;

    @Column(nullable = false)
    @NotNull(message="is required")
    private String ownerEmail;

    // define constructors

    public Transaction() {

    }

    public Transaction(BigDecimal amount, BigDecimal balance, String type, LocalDateTime timestamp, String ownerEmail) {
        this.amount = amount;
        this.balance = balance;
        this.type = type;
        this.timestamp = timestamp;
        this.ownerEmail = ownerEmail;
    }

    // define getters/setters

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
                ", balance=" + balance +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", ownerEmail='" + ownerEmail + '\'' +
                '}';
    }

}
