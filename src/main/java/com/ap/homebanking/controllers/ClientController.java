package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.ap.homebanking.utils.Util.getRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

//    @Autowired
//    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        //return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
        return clientService.getClients();
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        //return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
        return clientService.getClient(id);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password
    ){
        if (firstName.isEmpty()){
            return new ResponseEntity<>("Missing value: enter your first name", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()){
            return new ResponseEntity<>("Missing value: enter your last name", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()){
            return new ResponseEntity<>("Missing value: enter your email", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()){
            return new ResponseEntity<>("Missing value: enter your password", HttpStatus.FORBIDDEN);
        }

        //if (clientRepository.findByEmail(email) != null){
        if (clientService.findByEmail(email) != null){
            return new ResponseEntity<>("email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.save(client);

        Integer number = getRandomNumber(0, 99999999);
        String accountNumber = "VIN-" + number;
        Account account = new Account(accountNumber, LocalDate.now(), 0);
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){

        Client authenticated = clientService.findByEmail(authentication.getName());

        return new ClientDTO(authenticated);

    }

}
