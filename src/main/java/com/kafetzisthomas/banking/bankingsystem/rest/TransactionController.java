package com.kafetzisthomas.banking.bankingsystem.rest;

import com.kafetzisthomas.banking.bankingsystem.entity.Transaction;
import com.kafetzisthomas.banking.bankingsystem.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String listTransactions(HttpServletRequest request, Model theModel, Principal principal) {

        // get the transactions from db only for the current user
        List<Transaction> transactions = transactionService.getAllTransactions(principal.getName());

        theModel.addAttribute("transactions", transactions);
        theModel.addAttribute("request", request);
        return "transactions/transaction-report";
    }

    @GetMapping("/deposit")
    public String showDepositForm(HttpServletRequest request, Model theModel) {
        theModel.addAttribute("transaction", new Transaction());
        theModel.addAttribute("request", request);
        return "transactions/deposit-form";
    }

    @GetMapping("/withdraw")
    public String showWithdrawForm(HttpServletRequest request, Model theModel) {
        theModel.addAttribute("transaction", new Transaction());
        theModel.addAttribute("request", request);
        return "transactions/withdraw-form";
    }

    @PostMapping("/deposit/save")
    public String saveDeposit(@ModelAttribute("transaction") Transaction theTransaction,
                              @Valid BindingResult theBindingResult, Principal principal) {

        if (theBindingResult.hasErrors()) {
            return "transactions/deposit-form";
        }

        transactionService.deposit(theTransaction, principal.getName());
        return "redirect:/";
    }

    @PostMapping("/withdraw/save")
    public String saveWithdraw(@ModelAttribute("transaction") Transaction theTransaction,
                               @Valid BindingResult theBindingResult, Principal principal) {

        if (theBindingResult.hasErrors()) {
            return "transactions/withdraw-form";
        }

        transactionService.withdraw(theTransaction, principal.getName());
        return "redirect:/";
    }

}
