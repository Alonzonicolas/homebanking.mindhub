package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentClientCards(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Set<Card> clientCards = client.getCards();

        List<CardDTO> cardDTOS = clientCards
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(cardDTOS, HttpStatus.ACCEPTED);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        if (cardColor != CardColor.GOLD && cardColor != CardColor.SILVER && cardColor != CardColor.TITANIUM) {
            return new ResponseEntity<>("Card color must be GOLD, SILVER or TITANIUM", HttpStatus.FORBIDDEN);
        }

        if (cardType != CardType.CREDIT && cardType != CardType.DEBIT) {
            return new ResponseEntity<>("Card type must be CREDIT or DEBIT", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail((authentication.getName()));

        boolean cardExists = client.getCards().stream()
                .anyMatch(card -> card.getType() == cardType && card.getColor() == cardColor);

        if (cardExists) {
            return new ResponseEntity<>("A card of this type and color already exists", HttpStatus.FORBIDDEN);
        }

        String cardNumber;

        do {
            cardNumber = CardUtils.getCardNumber();
        } while (cardService.existsByNumber(cardNumber));

        Card card = new Card(client.getFirstName()+" "+client.getLastName(), cardType, cardColor, cardNumber, CardUtils.getCVV(), LocalDateTime.now().plusYears(5), LocalDateTime.now());
        cardService.save(card);
        client.addCard(card);
        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static String getCardNumber(int min, int max){
        return getRandomNumber(min, max)+"-"+getRandomNumber(min, max)+"-"+getRandomNumber(min, max)+"-"+getRandomNumber(min, max);
    }*/

}
