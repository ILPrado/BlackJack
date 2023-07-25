package ar.edu.utn.frc.tup.lciii.dtos.match;

import ar.edu.utn.frc.tup.lciii.dtos.player.PlayerResponseDTO;
import ar.edu.utn.frc.tup.lciii.models.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDTO {

    private Long id;
    private Integer playerScore;
    private String playerHand;
    private String appFirstCard;
    private BigDecimal playerBet;
    private PlayerResponseDTO player;
    private PlayerResponseDTO app;
    private PlayerResponseDTO winner;
    private Status matchStatus;
}
