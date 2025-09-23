package com.kafetzisthomas.banking.bankingsystem.rest;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import com.kafetzisthomas.banking.bankingsystem.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String listTransactions(Model theModel) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        theModel.addAttribute("transactions", transactions);
        return "transaction-report";
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/addTransaction")
    public String showFormForAdd(Model theModel) {

        // create a model attribute to bind form data
        Transaction theTransaction = new Transaction();

        theModel.addAttribute("transaction", theTransaction);

        return "deposit-form";
    }

    @PostMapping("/save")
    public String saveTransaction(@ModelAttribute("transaction") Transaction theTransaction) {
        transactionService.createTransaction(theTransaction);

        // use a redirect to prevent duplicate submissions
        return "redirect:/";
    }

    @PutMapping("/transactions/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        return transactionService.updateTransaction(id, updatedTransaction);
    }

    @DeleteMapping("/transactions/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
