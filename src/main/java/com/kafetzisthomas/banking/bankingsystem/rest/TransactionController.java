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

    @GetMapping("/deposit")
    public String showDepositForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "deposit-form";
    }

    @GetMapping("/withdraw")
    public String showWithdrawForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "withdraw-form";
    }

    @PostMapping("/deposit/save")
    public String saveDeposit(@ModelAttribute("transaction") Transaction theTransaction) {
        transactionService.deposit(theTransaction);
        return "redirect:/";
    }

    @PostMapping("/withdraw/save")
    public String saveWithdraw(@ModelAttribute("transaction") Transaction theTransaction) {
        transactionService.withdraw(theTransaction);
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
