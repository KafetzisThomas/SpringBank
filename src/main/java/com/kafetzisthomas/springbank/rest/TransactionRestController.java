package com.kafetzisthomas.springbank.rest;

import com.kafetzisthomas.springbank.entity.Transaction;
import com.kafetzisthomas.springbank.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getTransactions(@RequestParam(required = false) String daterange, Principal principal) {
        if (daterange != null && !daterange.isBlank()) {
            return transactionService.getTransactionsByDateRange(principal.getName(), daterange);
        }
        return transactionService.getAllTransactions(principal.getName());
    }

    public record AmountRequest(java.math.BigDecimal amount) {}

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody AmountRequest request, Principal principal) {
        try {
            Transaction transaction = new Transaction();
            transaction.setAmount(request.amount());

            transactionService.deposit(transaction, principal.getName());
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception err) {
            return ResponseEntity.badRequest().body("Deposit failed: " + err.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody AmountRequest request, Principal principal) {
        try {
            Transaction transaction = new Transaction();
            transaction.setAmount(request.amount());

            transactionService.withdraw(transaction, principal.getName());
            return ResponseEntity.ok("Withdraw successful");
        } catch (Exception err) {
            return ResponseEntity.badRequest().body("Withdraw failed: " + err.getMessage());
        }
    }

}
