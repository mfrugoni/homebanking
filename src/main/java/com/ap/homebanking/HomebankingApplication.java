package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args -> {

			//Melba Morel Data:
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");

			LocalDate today = LocalDate.now();
			Account account1 = new Account("VIN001", today, 5000);

			Transaction trans1 = new Transaction(TransactionType.CREDIT, 2500, "lottery", LocalDateTime.now());
			Transaction trans2 = new Transaction(TransactionType.DEBIT, -25.44, "beer", LocalDateTime.now());

			LocalDate tomorrow = today.plusDays(1);
			Account account2 = new Account("VIN002", tomorrow, 7500);

			Transaction trans3 = new Transaction(TransactionType.CREDIT, 12500, "BINGO", LocalDateTime.now());
			Transaction trans4 = new Transaction(TransactionType.DEBIT, -1125.44, "TAXES", LocalDateTime.now());

			clientRepository.save(client1);
			client1.addAccount(account1);
			client1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			account1.addTransaction(trans1);
			account1.addTransaction(trans2);
			account2.addTransaction(trans3);
			account2.addTransaction(trans4);
			transactionRepository.save(trans1);
			transactionRepository.save(trans2);
			transactionRepository.save(trans3);
			transactionRepository.save(trans4);



			Client client2 = new Client("Mikaela", "Schiffrin", "mika@email.com");

			Account account3 = new Account("AA12", today, 12300);

			Transaction trans5 = new Transaction(TransactionType.CREDIT, 15598, "RACE", LocalDateTime.now());
			Transaction trans6 = new Transaction(TransactionType.DEBIT, -598, "gas", LocalDateTime.now());

			clientRepository.save(client2);
			client2.addAccount(account3);
			accountRepository.save(account3);
			account3.addTransaction(trans5);
			account3.addTransaction(trans6);
			transactionRepository.save(trans5);
			transactionRepository.save(trans6);

		});
	}

}
