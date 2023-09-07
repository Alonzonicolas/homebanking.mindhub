package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    void save(Card card);
    boolean existsByNumber(String cardNumber);

}
