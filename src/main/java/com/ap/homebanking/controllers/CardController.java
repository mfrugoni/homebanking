package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.CardDTO;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
            return new ResponseEntity<>("You are not allowed to own more than three debit cards", HttpStatus.FORBIDDEN);

        if (creditCards.size() >= 3)
            return new ResponseEntity<>("You are not allowed to own more than three credit cards", HttpStatus.FORBIDDEN);

        String cardHolder = authenticated.getFirstName() + " " + authenticated.getLastName();

        String firstSection = createNumberSection();
        String secondSection = createNumberSection();
        String thirdSection = createNumberSection();
        String fourthSection = createNumberSection();
        String cardNumber = firstSection + " " + secondSection + " " + thirdSection + " " + fourthSection;

        LocalDate fromDate = LocalDate.now();

        LocalDate thruDate = LocalDate.now().plusYears(5);

        String cvv = createCvv();

        Card createdCard = new Card(cardHolder, type, color, cardNumber, cvv, fromDate, thruDate);
        authenticated.addCard(createdCard);
        cardRepository.save(createdCard);

        return new ResponseEntity<>(new CardDTO(createdCard), HttpStatus.CREATED);

    }

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        Client authenticated = clientRepository.findByEmail(authentication.getName());
        return authenticated.getCards().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String createCvv(){
        Integer randomNumber = getRandomNumber(0, 999);

        if (randomNumber < 100 && randomNumber > 10)
            return "0" + randomNumber;
        if (randomNumber < 10)
            return "00" + randomNumber;
        else
            return randomNumber.toString();

    }

    public String createNumberSection(){
        Integer numberSection = getRandomNumber(0, 9999);
        String section;

        if (numberSection < 1000 && numberSection > 99){
            section = "0" + numberSection;
            return section;
        }
        if (numberSection < 100 && numberSection > 9){
            section = "00" + numberSection;
            return section;
        }

        if (numberSection < 10){
            section = "000" + numberSection;
            return section;
        }
        else
            return numberSection.toString();
    }

}
