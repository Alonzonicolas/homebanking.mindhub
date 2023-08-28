package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        List<Account> allAccounts = accountRepository.findAll();

        List<AccountDTO> convertedList = allAccounts
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return convertedList;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id, Authentication authentication){
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientRepository.findByEmail((authentication.getName()));
        Account account = accountRepository.findById(id).orElse(null);
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

        Client client = clientRepository.findByEmail(authentication.getName());
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

        Client client = clientRepository.findByEmail((authentication.getName()));

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You reached the maximum number of accounts allowed", HttpStatus.FORBIDDEN);
        }

        String accountNumber;

        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByNumber(accountNumber));

        Account account = accountRepository.save(new Account(accountNumber, LocalDateTime.now() , 0));
        client.addAccount(account);
        clientRepository.save(client);
        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

    public String generateAccountNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000000);
        return String.format("%08d", randomNumber);
    }
}