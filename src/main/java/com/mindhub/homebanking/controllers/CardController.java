package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentClientCards(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
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

        Client client = clientRepository.findByEmail((authentication.getName()));

        boolean cardExists = client.getCards().stream()
                .anyMatch(card -> card.getType() == cardType && card.getColor() == cardColor);

        if (cardExists) {
            return new ResponseEntity<>("A card of this type and color already exists", HttpStatus.FORBIDDEN);
        }

        String cardNumber;

        do {
            cardNumber = generateCardNumber(1000, 9999);
        } while (cardRepository.existsByNumber(cardNumber));

        Card card = cardRepository.save(new Card(client.getFirstName()+" "+client.getLastName(), cardType, cardColor, cardNumber, getRandomNumber(100, 999), LocalDateTime.now().plusYears(5), LocalDateTime.now()));
        client.addCard(card);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public String generateCardNumber(int min, int max){
        return getRandomNumber(min, max)+"-"+getRandomNumber(min, max)+"-"+getRandomNumber(min, max)+"-"+getRandomNumber(min, max);
    }
}
