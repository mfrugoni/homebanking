package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.services.AccountService;
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

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClients();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClient(id);
    }

    @PostMapping("/clients")
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

        if (clientService.findByEmail(email) != null){
            return new ResponseEntity<>("email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.save(client);

        Integer number = getRandomNumber(0, 99999999);
        String accountNumber = "VIN-" + number;
        Account account = new Account(accountNumber, LocalDate.now(), 0);
        client.addAccount(account);
        accountService.save(account);

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){

        Client authenticated = clientService.findByEmail(authentication.getName());

        return new ClientDTO(authenticated);

    }

}
