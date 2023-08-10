package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client client1 = clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
			Client client2 = clientRepository.save(new Client("Jack", "Bauer", "jack@mindhub.com"));
			Client client3 = clientRepository.save(new Client("Chloe", "O'Brian", "chloe@mindhub.com"));
			Client client4 = clientRepository.save(new Client("Kim", "Bauer", "kim@mindhub.com"));
			Client client5 = clientRepository.save(new Client("David", "Palmer", "david@mindhub.com"));
			Client client6 = clientRepository.save(new Client("Michelle", "Dessler", "michelle@mindhub.com"));

			Account account1 = new Account("VIN001", LocalDate.now() , 5000);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1) , 7500);
			client1.addAccount(account2);
			accountRepository.save(account2);
			Account account3 = new Account("VIN043", LocalDate.now(), 3400);
			client2.addAccount(account3);
			accountRepository.save(account3);
			Account account4 = new Account("VIN042", LocalDate.now() , 2200);
			client3.addAccount(account4);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 4000, "Sales", LocalDate.now());
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -1500, "Location", LocalDate.now());
			account1.addTransaction(transaction2);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -540, "Debt", LocalDate.now());
			account2.addTransaction(transaction3);
			transactionRepository.save(transaction3);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 1360, "", LocalDate.now());
			account2.addTransaction(transaction4);
			transactionRepository.save(transaction4);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -4700, "Store", LocalDate.now());
			account2.addTransaction(transaction5);
			transactionRepository.save(transaction5);

		};
	}
}
