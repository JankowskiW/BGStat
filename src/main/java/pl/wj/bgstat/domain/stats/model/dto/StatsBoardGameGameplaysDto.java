package pl.wj.bgstat.domain.stats.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsBoardGameGameplaysDto {
    private long boardGameId;
    private String boardGameName;
    /**
     * Represents the number of gameplays of board game in given period.
     */
    private int numOfGameplays;
    /**
     * Represents the gameplay average time in minutes of board game in given period
     */
    private int avgTimeOfGameplay;
}

