package ar.edu.utn.frc.tup.lciii.dtos.play;


import ar.edu.utn.frc.tup.lciii.models.Card;
import ar.edu.utn.frc.tup.lciii.models.Status;
import ar.edu.utn.frc.tup.lciii.models.PlayDecision;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayResponseDTO {

    @JsonProperty("player")
    private Long player;

    @JsonProperty("decision")
    private PlayDecision decision;

    @JsonProperty("player_hand")
    private String playerHand;

    @JsonProperty("player_score")
    private Integer playerScore;

    @JsonProperty("bet_player")
    private Integer betPlayer;

    @JsonProperty("app_hand")
    private String appHand;

    @JsonProperty("app_score")
    private Integer appScore;

    @JsonProperty("number_of_cards_in_deck")
    private Integer cardsInDeck;

    @JsonProperty("match_status")
    private Status matchStatus;

    @JsonProperty("winner")
    private String winner;

}
