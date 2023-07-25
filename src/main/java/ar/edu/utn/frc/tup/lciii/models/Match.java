package ar.edu.utn.frc.tup.lciii.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    private Long id;
    private Deck deck;
    private List<Card> playerHand;
    private List<Card> appHand;
    private Integer betPlayer;
    private Integer nextCardIndex;
    private Integer playId;
    private Player player;
    private Player appPlayer;
    private Player winner;
    private Status matchStatus;
}
