package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.models.Card;
import ar.edu.utn.frc.tup.lciii.models.CardSuit;
import ar.edu.utn.frc.tup.lciii.models.Deck;
import ar.edu.utn.frc.tup.lciii.services.DeckService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
@Service
public class DeckServiceImpl implements DeckService {

    @Override
    public Deck createDeck() {
        Deck deck = new Deck(new ArrayList<>());
        for(CardSuit cardSuit :CardSuit.values()) {
            for(int i = 2; i < 11; i++) {
                deck.getCards().add(new Card(cardSuit, String.valueOf(i), BigDecimal.valueOf(i)));
            }
        }
        for(CardSuit cardSuit :CardSuit.values()) {
            for(int i = 1; i < 5; i++) {
                if(i==1){
                    deck.getCards().add(new Card(cardSuit, "J", BigDecimal.valueOf(10)));
                }else if(i==2){
                    deck.getCards().add(new Card(cardSuit, "Q", BigDecimal.valueOf(10)));
                }else if(i==3){
                    deck.getCards().add(new Card(cardSuit, "K", BigDecimal.valueOf(10)));
                }else if(i==4){
                    deck.getCards().add(new Card(cardSuit, "A", BigDecimal.valueOf(1)));
                }

            }
        }

        return deck;
    }

    @Override
    public void shuffleDeck(Deck deck) {
        Collections.shuffle(deck.getCards());
    }

    @Override
    public Card takeCard(Deck deck, Integer deckIndex) {
        return deck.getCards().get(deckIndex);
    }
}
