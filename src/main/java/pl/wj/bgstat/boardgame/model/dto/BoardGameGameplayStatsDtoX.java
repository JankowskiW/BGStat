package pl.wj.bgstat.boardgame.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardGameGameplayStatsDtoX {
    private long boardGameId;
    private String boardGameName;
    /**
     * Represents the number of gameplays in given period.
     */
    private int numOfGameplays;
    /**
     * Represents the average time in minutes of gameplay in given period
     */
    private int avgTimeOfGameplay;
}
