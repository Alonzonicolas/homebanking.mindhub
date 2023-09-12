package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	/*@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Client client1 = clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123")));
			Client client2 = clientRepository.save(new Client("Jack", "Bauer", "jack@mindhub.com", passwordEncoder.encode("123")));
			Client client3 = clientRepository.save(new Client("Chloe", "O'Brian", "chloe@mindhub.com", passwordEncoder.encode("123")));
			Client client4 = clientRepository.save(new Client("Kim", "Bauer", "kim@mindhub.com", passwordEncoder.encode("123")));
			Client client5 = clientRepository.save(new Client("David", "Palmer", "david@mindhub.com", passwordEncoder.encode("123")));
			Client client6 = clientRepository.save(new Client("Michelle", "Dessler", "michelle@mindhub.com", passwordEncoder.encode("123")));
			Client client7 = clientRepository.save(new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("123")));

			Account account1 = new Account("VIN-00141421", LocalDateTime.now() , 5000);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN-31264002", LocalDateTime.now().plusDays(1) , 7500);
			client1.addAccount(account2);
			accountRepository.save(account2);
			Account account3 = new Account("VIN-45724043", LocalDateTime.now(), 3400);
			client2.addAccount(account3);
			accountRepository.save(account3);
			Account account4 = new Account("VIN-62085042", LocalDateTime.now() , 2200);
			client3.addAccount(account4);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 4000, "Sales", LocalDateTime.now());
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -1500, "Location", LocalDateTime.now());
			account1.addTransaction(transaction2);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -540, "Debt", LocalDateTime.now());
			account2.addTransaction(transaction3);
			transactionRepository.save(transaction3);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 1360, "Sales", LocalDateTime.now());
			account2.addTransaction(transaction4);
			transactionRepository.save(transaction4);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -4700, "Store", LocalDateTime.now());
			account2.addTransaction(transaction5);
			transactionRepository.save(transaction5);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -1500, "Store", LocalDateTime.now());
			account3.addTransaction(transaction6);
			transactionRepository.save(transaction6);

			Loan loan1 = new Loan("Mortgage", 500000, List.of(12, 24, 36, 48, 60));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal", 100000, List.of(6, 12, 24));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Auto", 300000, List.of(6, 12, 24, 36));
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000, loan1.getPayments().get(4));
			loan1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan(50000, loan2.getPayments().get(1));
			loan2.addClientLoan(clientLoan2);
			client1.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000, loan2.getPayments().get(2));
			loan2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4 = new ClientLoan(200000, loan3.getPayments().get(3));
			loan3.addClientLoan(clientLoan4);
			client2.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "1341-5325-2523-2311", 321, LocalDateTime.now().plusYears(5), LocalDateTime.now());
			client1.addCard(card1);
			cardRepository.save(card1);
			Card card2 = new Card(client1.getFirstName()+" "+client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "3536-7965-0650-4503", 755, LocalDateTime.now().plusYears(5), LocalDateTime.now());
			client1.addCard(card2);
			cardRepository.save(card2);
			Card card3 = new Card(client2.getFirstName()+" "+client2.getLastName(), CardType.CREDIT, CardColor.SILVER, "3145-6215-1212-0325", 332, LocalDateTime.now().plusYears(6), LocalDateTime.now());
			client2.addCard(card3);
			cardRepository.save(card3);
		};
	}*/
}
