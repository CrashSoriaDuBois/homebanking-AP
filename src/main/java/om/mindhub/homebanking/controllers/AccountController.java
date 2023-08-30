package om.mindhub.homebanking.controllers;

import om.mindhub.homebanking.dtos.AccountDTO;
import om.mindhub.homebanking.models.Account;
import om.mindhub.homebanking.repositories.AccountRepository;
import om.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getClients() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getClient(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
    @RequestMapping(path ="/clients/current/accounts" ,method = RequestMethod.POST)
    public ResponseEntity<Object> createCurrentAccount(Authentication authentication){
        //max of accounts
        if(accountRepository.findByClientEmail(authentication.getName()).size()>=3){
            return new ResponseEntity<>("User has 3 accounts", HttpStatus.FORBIDDEN);
        }
        //create account
        String accountNumber = createNumberAccount();
        Account account = new Account(accountNumber, LocalDateTime.now(),0.0);
        account.setClient(clientRepository.findByEmail(authentication.getName()));
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    private String createNumberAccount(){
        String accountNumber;
        do {
            int random = (int) (Math.random() * 99999999);
            accountNumber = "VIN-" + random;
        } while (accountRepository.existsByNumber(accountNumber));
        return accountNumber;
    }
    @RequestMapping(path ="/clients/current/accounts")
    public List<AccountDTO> getClientAccount(Authentication authentication){
        return accountRepository.findByClientEmail(authentication.getName()).stream().map(AccountDTO::new).collect(toList());
    }

}
