package ar.edu.utn.frc.tup.lciii.entities;


import ar.edu.utn.frc.tup.lciii.models.Card;
import ar.edu.utn.frc.tup.lciii.models.Deck;
import ar.edu.utn.frc.tup.lciii.models.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "matches")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = DeckConverter.class)
    @Column(length = 1000)
    private Deck deck;

    @Lob
    @Convert(converter = HandConverter.class)
    @Column
    private List<Card> playerHand;

    @Lob
    @Convert(converter = HandConverter.class)
    @Column
    private List<Card> appHand;

    @Column
    private Integer betPlayer;

    @Column
    private Integer nextCardIndex;

    @Column
    private Integer playId;

    @JoinColumn(name="player_id")
    @ManyToOne
    private PlayerEntity player;

    @JoinColumn(name="app_player_id")
    @ManyToOne
    private PlayerEntity appPlayer;

    @JoinColumn(name="winner_id")
    @ManyToOne
    private PlayerEntity winner;

    @Column
    @Enumerated(EnumType.STRING)
    private Status matchStatus;
}
