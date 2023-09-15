package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id, Authentication authentication){
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail((authentication.getName()));
        Account account = accountService.findById(id);
        if (account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (account.getClient().equals(client)){
            AccountDTO accountDTO = new AccountDTO(account);
            return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Unauthorized data", HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<Object> getCurrentClientAccounts(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Set<Account> clientAccounts = client.getAccounts();

        List<AccountDTO> accountDTOS = clientAccounts
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(accountDTOS, HttpStatus.ACCEPTED);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail((authentication.getName()));

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You reached the maximum number of accounts allowed", HttpStatus.FORBIDDEN);
        }

        String accountNumber;

        do {
            accountNumber = AccountUtils.generateAccountNumber();
        } while (accountService.existsByNumber(accountNumber));

        Account account = new Account(accountNumber, LocalDateTime.now() , 0);
        accountService.save(account);
        client.addAccount(account);
        clientService.save(client);
        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

}