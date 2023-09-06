package om.mindhub.homebanking.controllers;

import om.mindhub.homebanking.dtos.CardDTO;
import om.mindhub.homebanking.enums.CardColor;
import om.mindhub.homebanking.enums.CardType;
import om.mindhub.homebanking.models.Card;
import om.mindhub.homebanking.models.Client;
import om.mindhub.homebanking.repositories.CardRepository;
import om.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/cards")
    public List<CardDTO> getAllCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCurrentClientCards(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().map(CardDTO::new).collect(toList());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCurrentCard(
            @RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        //validate
        boolean hasClientAuthority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("CLIENT"));
        if(!hasClientAuthority) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        if (cardRepository.existsByTypeAndColorAndClient(cardType, cardColor, client)) {
            return new ResponseEntity<>("card alredy exist", HttpStatus.FORBIDDEN);
        }
        //create card
        Card newCard = new Card(client.cardHolder(), cardType, cardColor,
                generateNumber(), generateCvv(), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
        client.addCard(newCard);
        cardRepository.save(newCard);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private int generateCvv(){
        return (int) (Math.random() * 999);
    }

    private String generateNumber(){
        DecimalFormat format=new DecimalFormat("0000");
        String number="";
        for(int i=0;i<4;i++){
            number += format.format((int)(Math.random() * 9999));
            if(i!=3){
                number+="-";
            }
        }
        return number;
    }

}
