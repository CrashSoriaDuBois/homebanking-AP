package om.mindhub.homebanking;

import om.mindhub.homebanking.models.Client;
import om.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return (args -> {
			Client client1 = new Client(1, "Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(client1);

			clientRepository.save(new Client(2, "Ines", "Risotto", "inessirotto@gmail.com"));
	});
	}
}
