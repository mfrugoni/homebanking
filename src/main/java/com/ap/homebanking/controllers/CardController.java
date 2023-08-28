package com.ap.homebanking.controllers;

import com.ap.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard
}
