package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            @RequestParam CardColor color,
            @RequestParam CardType type,
            Authentication authentication){

        Client authenticated = clientRepository.findByEmail(authentication.getName());

        Set<Card> totalCards = new HashSet<>();
        totalCards = authenticated.getCards();

        List<Card> debitCards = new ArrayList<>();
        debitCards = totalCards.stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toList());

        List<Card> creditCards = new ArrayList<>();
        creditCards = totalCards.stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toList());

        if (debitCards.size() >= 3)
            return new ResponseEntity<>("You are not allowed to have more than three debit cards", HttpStatus.FORBIDDEN);

        if (creditCards.size() >= 3)
            return new ResponseEntity<>("You are not allowed to have more than three credit cards", HttpStatus.FORBIDDEN);

        int cvv = getRandomNumber(000, 999);


    }

    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }


}
