package om.mindhub.homebanking;

import om.mindhub.homebanking.dtos.AccountDTO;
import om.mindhub.homebanking.models.*;
import om.mindhub.homebanking.enums.TransactionType;
import om.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository){
		return (args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Account account1 = new Account("VIN001", LocalDate.now(),5000 );
			Account account2 = new Account("VIN002", (LocalDate.now()).plusDays(1),7500 );

			client1.addAccounts(account1);
			client1.addAccounts(account2);
			clientRepository.save(client1);
			accountRepository.save(account1);
			accountRepository.save(account2);

			Client client2 = new Client("Ines", "Risotto", "inessirotto@gmail.com");
			Account account3 = new Account("VIN003", LocalDate.now(),2500 );
			Account account4 = new Account("VIN004", (LocalDate.now()).plusDays(1),1000 );


			client2.addAccounts(account3);
			client2.addAccounts(account4);
			clientRepository.save(client2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT,5000,"description", LocalDateTime.now(),account1);
			transactionRepository.save(transaction1);

			Transaction transaction2 = new Transaction(TransactionType.CREDIT,5000,"other description",LocalDateTime.now(),account2);
			transactionRepository.save(transaction2);

			//
			Loan loan1 = new Loan("Mortgage", 500000, List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal", 500000, List.of(6,12,24));
			Loan loan3 = new Loan("Automotive", 500000, List.of(12,24,36,48,60));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			ClientLoan clientLoan1 = new ClientLoan(loan1.getName(),400000,loan1.getPayments().get(4));
			ClientLoan clientLoan2 = new ClientLoan(loan2.getName(),50000,loan2.getPayments().get(1));
			ClientLoan clientLoan3 = new ClientLoan(loan2.getName(),100000,loan2.getPayments().get(2));
			ClientLoan clientLoan4 = new ClientLoan(loan2.getName(),200000,loan3.getPayments().get(2));


			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			clientRepository.save(client1);
			clientRepository.save(client2);


			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
	});
	}
}
