package com.kafetzisthomas.springbank.rest;

import com.kafetzisthomas.springbank.entity.Transaction;
import com.kafetzisthomas.springbank.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String listTransactions(@RequestParam(required = false) String daterange, HttpServletRequest request, Model theModel, Principal principal) {
        List<Transaction> transactions;
        BigDecimal currentBalance = BigDecimal.ZERO;

        if (daterange != null && !daterange.isBlank()) {
            transactions = transactionService.getTransactionsByDateRange(principal.getName(), daterange);

            if (transactions.isEmpty()) {
                theModel.addAttribute("errorMessage", "No transactions found for selected date range");
                transactions = transactionService.getAllTransactions(principal.getName());
            }

        } else {
            transactions = transactionService.getAllTransactions(principal.getName());
        }

        theModel.addAttribute("transactions", transactions);
        theModel.addAttribute("request", request);
        theModel.addAttribute("daterange", daterange);

        if (!transactions.isEmpty()) {
            currentBalance = transactions.getLast().getBalance();
        } else {
            currentBalance = BigDecimal.ZERO;
        }

        theModel.addAttribute("currentBalance", currentBalance);

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
    public String saveDeposit(@Valid @ModelAttribute("transaction") Transaction theTransaction, BindingResult theBindingResult, Principal principal, RedirectAttributes redirectAttributes) {
        if (theBindingResult.hasErrors()) {
            return "transactions/deposit-form";
        }

        try {
            transactionService.deposit(theTransaction, principal.getName());
            String formattedAmount = theTransaction.getAmount() + " €";
            redirectAttributes.addFlashAttribute("successMessage", "Deposit successful: " + formattedAmount + " added to your account");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Deposit failed: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/withdraw/save")
    public String saveWithdraw(@Valid @ModelAttribute("transaction") Transaction theTransaction, BindingResult theBindingResult, Principal principal, RedirectAttributes redirectAttributes) {
        if (theBindingResult.hasErrors()) {
            return "transactions/withdraw-form";
        }

        try {
            transactionService.withdraw(theTransaction, principal.getName());
            String formattedAmount = theTransaction.getAmount() + " €";
            redirectAttributes.addFlashAttribute("successMessage", "Withdraw successful: " + formattedAmount + " removed from your account");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Withdraw failed: " + e.getMessage());
        }
        return "redirect:/";
    }

}
