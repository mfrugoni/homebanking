package com.ap.homebanking;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository){
		return (args -> {

			//Melba Morel Data:
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));

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

			//Mika Schiffrin Data:
			Client client2 = new Client("Mikaela", "Schiffrin", "mika@email.com", passwordEncoder.encode("mika"));

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


			Loan loanHipotecario = new Loan("Hipotecario", 500000, List.of(12, 24, 36, 48, 60));
			Loan loanPersonal = new Loan("Personal", 100000, List.of(6, 12, 24));
			Loan loanAutomotriz = new Loan("Automotriz", 300000, List.of(6, 12, 24, 36));

			loanRepository.save(loanHipotecario);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanAutomotriz);

			ClientLoan melbaLoanHipotecario = new ClientLoan(400000, 60, client1, loanHipotecario);
			ClientLoan melbaLoanPersonal = new ClientLoan(50000, 12, client1, loanPersonal);

			ClientLoan mikaLoanPersonal = new ClientLoan(100000, 24, client2, loanPersonal);
			ClientLoan mikaLoanAuto = new ClientLoan(200000, 36, client2, loanAutomotriz);

			client1.addClientLoan(melbaLoanHipotecario);
			client1.addClientLoan(melbaLoanPersonal);

			client2.addClientLoan(mikaLoanPersonal);
			client2.addClientLoan(mikaLoanAuto);

			clientLoanRepository.save(melbaLoanHipotecario);
			clientLoanRepository.save(melbaLoanPersonal);
			clientLoanRepository.save(mikaLoanPersonal);
			clientLoanRepository.save(mikaLoanAuto);

			String melbaCardHolder = client1.getFirstName() + " " + client1.getLastName();
			Card melbaGoldDebit = new Card(melbaCardHolder, CardType.DEBIT, CardColor.GOLD, "5555666677778888", 773, LocalDate.now(), LocalDate.now().plusYears(5));
			Card melbaTitaniumCredit = new Card(melbaCardHolder, CardType.CREDIT, CardColor.TITANIUM, "1212333310293847", 182, LocalDate.now(), LocalDate.now().plusYears(5));

			String mikaCardHolder = client2.getFirstName() + " " + client2.getLastName();
			Card mikaSilverCredit = new Card(mikaCardHolder, CardType.CREDIT, CardColor.SILVER, "9201837475660101", 555, LocalDate.now(), LocalDate.now().plusYears(4));

			client1.addCard(melbaGoldDebit);
			client1.addCard(melbaTitaniumCredit);
			client2.addCard(mikaSilverCredit);

			cardRepository.save(melbaGoldDebit);
			cardRepository.save(melbaTitaniumCredit);
			cardRepository.save(mikaSilverCredit);

			Client clientAdmin = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin"));
			clientRepository.save(clientAdmin);

		});
	}

}
