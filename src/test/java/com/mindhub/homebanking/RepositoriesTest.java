package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void clientsHasAEmail() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            assertThat(client.getEmail(), is(notNullValue()));
        }
    }

    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void accountsHasAClient() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            assertThat(account.getClient(), is(not(nullValue())));
        }
    }

    @Test
    public void cardValidityTime() {
        List<Card> cards = cardRepository.findAll();
        for (Card card : cards) {
            LocalDateTime fromDate = card.getFromDate();
            LocalDateTime thruDate = card.getThruDate();
            long yearsDifference = Duration.between(fromDate, thruDate).toDays() / 365;
            assertThat(yearsDifference, greaterThanOrEqualTo(5L));
        }
    }

    @Test
    public void cardColorIsValid() {
        List<Card> cards = cardRepository.findAll();
        List<CardColor> validColors = Arrays.asList(CardColor.values());
        for (Card card : cards) {
            CardColor color = card.getColor();
            assertThat(color, is(notNullValue()));
            assertThat(validColors, hasItem(color));
        }
    }

    @Test
    public void transactionHasAAccount() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            assertThat(transaction.getAccount(), is(notNullValue()));
        }
    }

    @Test
    public void transactionAmountIsNotEmpty() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            assertThat(transaction.getAmount(), is(not(nullValue())));
        }
    }

}
