package om.mindhub.homebanking.controllers;
import om.mindhub.homebanking.dtos.ClienDTO;
import om.mindhub.homebanking.models.Account;
import om.mindhub.homebanking.models.Client;
import om.mindhub.homebanking.repositories.AccountRepository;
import om.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "api")
public class ClientController {
    @Autowired
    private ClientRepository cLrepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/clients")
    public List<ClienDTO> getClients() {
        return cLrepo.findAll().stream().map(ClienDTO::new).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClienDTO getClient(@PathVariable Long id){
        return cLrepo.findById(id).map(ClienDTO::new).orElse(null);
    }
    @GetMapping("/clients/current")
    public ClienDTO getClientCurrent(Authentication authentication){
        return new ClienDTO(cLrepo.findByEmail(authentication.getName()));
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> registerClient(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (cLrepo.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        String accountNumber = accountController.createNumberAccount();
        //create new account
        Account account = new Account(accountNumber, LocalDateTime.now(),0.0);
        //save account
        accountRepository.save(account);
        //create new client
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        client.addAccount(account);
        //save client
        cLrepo.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
