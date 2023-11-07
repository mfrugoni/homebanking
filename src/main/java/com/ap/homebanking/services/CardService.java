package com.ap.homebanking.services;

import com.ap.homebanking.models.Card;

public interface CardService {
    void save (Card card);
    Card findById(Long id);
    Card findByNumber(String number);
}
