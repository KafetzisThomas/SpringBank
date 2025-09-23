package com.kafetzisthomas.banking.bankingsystem.rest;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import com.kafetzisthomas.banking.bankingsystem.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String listTransactions(Model theModel, Principal principal) {

        // get the transactions from db only for the current user
        List<Transaction> transactions = transactionService.getAllTransactions(principal.getName());

        theModel.addAttribute("transactions", transactions);

        return "transactions/transaction-report";
    }

    @GetMapping("/deposit")
    public String showDepositForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/deposit-form";
    }

    @GetMapping("/withdraw")
    public String showWithdrawForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/withdraw-form";
    }

    @PostMapping("/deposit/save")
    public String saveDeposit(@ModelAttribute("transaction") Transaction theTransaction, Principal principal) {
        transactionService.deposit(theTransaction, principal.getName());
        return "redirect:/";
    }

    @PostMapping("/withdraw/save")
    public String saveWithdraw(@ModelAttribute("transaction") Transaction theTransaction, Principal principal) {
        transactionService.withdraw(theTransaction, principal.getName());
        return "redirect:/";
    }

}
