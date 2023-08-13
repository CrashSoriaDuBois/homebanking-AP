package om.mindhub.homebanking.dtos;

import om.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClienDTO {
    public long id;
    public String firstName;
    public String lastName;
    public String email;
    public Set<AccountDTO> accounts;
    public ClienDTO(){}
    public ClienDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());

    }
}
