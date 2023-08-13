package om.mindhub.homebanking.controllers;

import net.minidev.json.annotate.JsonIgnore;
import om.mindhub.homebanking.dtos.ClienDTO;
import om.mindhub.homebanking.models.Client;
import om.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api")
public class ClientController {
    @Autowired
    private ClientRepository repo;

    @RequestMapping("/clients")
    public List<ClienDTO> getClients() {
        return repo.findAll().stream().map(client -> new ClienDTO(client)).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClienDTO getClient(@PathVariable long id){
        return repo.findById(id).map(ClienDTO::new).orElse(null);
    }
}
