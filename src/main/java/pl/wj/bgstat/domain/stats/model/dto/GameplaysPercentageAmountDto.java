package pl.wj.bgstat.domain.stats.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameplaysPercentageAmountDto {
    private long boardGameId;
    private double percentageAmount;

    @JsonIgnore
    public double getFracPercAmount() {
        return percentageAmount - Math.floor(percentageAmount);
    }
}
