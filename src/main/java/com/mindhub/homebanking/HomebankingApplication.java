package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client client1 = clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
			Client client2 = clientRepository.save(new Client("Jack", "Bauer", "jack@mindhub.com"));
			Client client3 = clientRepository.save(new Client("Chloe", "O'Brian", "chloe@mindhub.com"));
			Client client4 = clientRepository.save(new Client("Kim", "Bauer", "kim@mindhub.com"));
			Client client5 = clientRepository.save(new Client("David", "Palmer", "david@mindhub.com"));
			Client client6 = clientRepository.save(new Client("Michelle", "Dessler", "michelle@mindhub.com"));

			Account account1 = new Account("CC0238", LocalDate.now() , 5000);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account("CC0688", LocalDate.now().plusDays(1) , 7500);
			client1.addAccount(account2);
			accountRepository.save(account2);
		};
	}
}
