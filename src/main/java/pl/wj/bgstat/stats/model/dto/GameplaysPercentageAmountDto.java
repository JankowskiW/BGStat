package pl.wj.bgstat.stats.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameplaysPercentageAmountDto {
    private long boardGameId;
    private double percentageAmount;
}
