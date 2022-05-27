package pl.wj.bgstat.stats.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsBoardGameGameplaysDto {
    long boardGameId;
    String boardGameName;
    /**
     * Represents the number of gameplays of board game in given period.
     */
    int numOfGameplays;
    /**
     * Represents the gameplay average time in minutes of board game in given period
     */
    int avgTimeOfGameplay;
}

