package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
                @RequestParam double amount,
                @RequestParam String description,
                @RequestParam String fromAccountNumber,
                @RequestParam String toAccountNumber,
                Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        if(amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Error: Parameters cannot be empty", HttpStatus.FORBIDDEN);
        }

        Account sourceAccount = accountService.findByNumber(fromAccountNumber);
        Account targetAccount = accountService.findByNumber(toAccountNumber);

        if(sourceAccount == null) {
            return new ResponseEntity<>("Error: Source account does not exist.",HttpStatus.FORBIDDEN);
        }

        if(targetAccount == null) {
            return new ResponseEntity<>("Error: Target account does not exist.",HttpStatus.FORBIDDEN);
        }

        if(sourceAccount.equals(targetAccount)) {
            return new ResponseEntity<>("Error: Account numbers cannot be the same.", HttpStatus.FORBIDDEN);
        }

        if(!authentication.getName().equals(sourceAccount.getClient().getEmail())) {
            return new ResponseEntity<>("Error: Account does not belong to the authenticated user.", HttpStatus.FORBIDDEN);
        }

        if(amount > sourceAccount.getBalance()) {
            return new ResponseEntity<>("You do not have sufficient funds to carry out the transaction", HttpStatus.FORBIDDEN);
        }

        Transaction sourceTransaction = new Transaction(TransactionType.DEBIT, -amount, targetAccount.getNumber() +" | "+ description, LocalDateTime.now());
        Transaction targetTransaction = new Transaction(TransactionType.CREDIT, amount, sourceAccount.getNumber() +" | "+ description, LocalDateTime.now());
        transactionService.save(sourceTransaction);
        transactionService.save(targetTransaction);

        sourceAccount.addTransaction(sourceTransaction);
        targetAccount.addTransaction(targetTransaction);
        sourceAccount.setBalance(sourceAccount.getBalance()-amount);
        targetAccount.setBalance((targetAccount.getBalance()+amount));
        accountService.save(sourceAccount);
        accountService.save(targetAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}