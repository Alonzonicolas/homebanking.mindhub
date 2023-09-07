package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() { return loanService.getLoansDTO(); }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loanApplication(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Authentication required.", HttpStatus.UNAUTHORIZED);
        }

        if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getToAccountNumber().isBlank()) {
            return new ResponseEntity<>("Error: Invalid loan application data.", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        Optional<Loan> optionalLoan = loanService.findById(loanApplicationDTO.getLoanId());
        double loanInterest = 1.2;

        if (account == null) {
            return new ResponseEntity<>("Error: Source account does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!authentication.getName().equals(account.getClient().getEmail())) {
            return new ResponseEntity<>("Error: Account does not belong to the authenticated user.", HttpStatus.FORBIDDEN);
        }

        if (optionalLoan.isPresent()) {

            Loan loan = optionalLoan.get();

            if(loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
                return new ResponseEntity<>("Error: The requested amount exceeds the maximum allowable loan amount required.", HttpStatus.FORBIDDEN);
            }

            if(!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
                return new ResponseEntity<>("Error: The number of installments is not available for the requested loan.", HttpStatus.FORBIDDEN);
            }

            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*loanInterest, loanApplicationDTO.getPayments());
            loan.addClientLoan(clientLoan);
            client.addClientLoan(clientLoan);
            clientLoanService.save(clientLoan);
            Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),  loan.getName()+" loan approved", LocalDateTime.now());
            transactionService.save(transaction);
            account.addTransaction(transaction);
            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            accountService.save(account);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Error: The type of loan does not exist", HttpStatus.FORBIDDEN);
        }
    }
}
