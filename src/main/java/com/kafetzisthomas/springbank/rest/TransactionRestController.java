package com.kafetzisthomas.springbank.rest;

import com.kafetzisthomas.springbank.entity.Transaction;
import com.kafetzisthomas.springbank.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByDateRange(principal.getName(), startDate, endDate));
        }
        return ResponseEntity.ok(transactionService.getAllTransactions(principal.getName()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Transaction transaction, Principal principal) {
        try {
            transactionService.deposit(transaction, principal.getName());
            return ResponseEntity.ok("Deposit successful");
        } catch (IllegalArgumentException err) {
            return ResponseEntity.badRequest().body("Deposit failed: " + err.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Transaction transaction, Principal principal) {
        try {
            transactionService.withdraw(transaction, principal.getName());
            return ResponseEntity.ok("Withdraw successful");
        } catch (IllegalArgumentException err) {
            return ResponseEntity.badRequest().body("Withdraw failed: " + err.getMessage());
        }
    }

}
