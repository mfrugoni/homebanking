package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args -> {

			//Melba Morel Data:
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");

			LocalDate today = LocalDate.now();
			Account account1 = new Account("VIN001", today, 5000);

			LocalDate tomorrow = today.plusDays(1);
			Account account2 = new Account("VIN002", tomorrow, 7500);

			clientRepository.save(client1);
			client1.addAccount(account1);
			client1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);


			Client client2 = new Client("Mikaela", "Schiffrin", "mika@email.com");

			Account account3 = new Account("AA12", today, 12300);

			clientRepository.save(client2);
			client2.addAccount(account3);
			accountRepository.save(account3);

		});
	}

}
