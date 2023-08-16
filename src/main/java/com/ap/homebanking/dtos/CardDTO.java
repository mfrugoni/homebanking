package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    public CardDTO(Card card){
        this.id = card.getId();

        this.cardHolder = card.getCardHolder();

        this.type = card.getType();

        this.color = card.getColor();

        this.number = card.getNumber();

        this.cvv = card.getCvv();

        this.fromDate = card.getFromDate();

        this.thruDate = card.getThruDate();
    }
}
