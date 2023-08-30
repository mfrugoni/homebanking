package com.ap.homebanking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(@RequestParam double amount, String description, String number, String number){

    }
}
