package ar.edu.utn.frc.tup.lciii.entities;

import ar.edu.utn.frc.tup.lciii.models.Card;
import ar.edu.utn.frc.tup.lciii.models.CardSuit;
import ar.edu.utn.frc.tup.lciii.models.Deck;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class HandConverter implements AttributeConverter<List<Card>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<Card> attribute) {
        if(attribute != null) {
            return attribute.stream().map(Card::toString).collect(Collectors.joining(SPLIT_CHAR));
        } else {
            return "";
        }
    }

   /* @Override
    public List<Card> convertToEntityAttribute(String dbData) {
        List<Card> list = new ArrayList<>();
        return new Deck(Arrays.stream(dbData.split(SPLIT_CHAR)).map(s -> {
            String [] cardMap = s.split("_");
            return new Card(
                    CardSuit.valueOf(cardMap[0]),
                    String.valueOf(cardMap[1]),
                    new BigDecimal(cardMap[2]));
        }).collect(Collectors.toList()));
    }*/
    @Override
    public List<Card> convertToEntityAttribute(String dbData) {
        List<Card> list = new ArrayList<>();
        String[] cardStrings = dbData.split(SPLIT_CHAR);

        for (String cardString : cardStrings) {
            Card card = mapToCard(cardString);
            list.add(card);
        }

        return list;
    }

    private Card mapToCard(String cardString) {
        String[] cardMap = cardString.split("_");
        CardSuit suit = CardSuit.valueOf(cardMap[0]);
        String number = cardMap[1];
        BigDecimal value = new BigDecimal(cardMap[2]);
        return new Card(suit, number, value);
    }
}
