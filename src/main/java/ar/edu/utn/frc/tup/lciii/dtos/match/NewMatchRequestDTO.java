package ar.edu.utn.frc.tup.lciii.dtos.match;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMatchRequestDTO {

    @NotNull
    private Long playerId;

    @NotNull
    private BigDecimal betPlayer;
}
