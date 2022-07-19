package pl.wj.bgstat.domain.gameplay.model.dto;

import lombok.*;
import pl.wj.bgstat.domain.boardgame.model.dto.BoardGameGameplaysStatsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameplaysStatsDto {
    /**
     * Represents given period of time
     */
    private LocalDate fromDate;
    private LocalDate toDate;

    /**
     * Represents the number of gameplays in given period.
     */
    private int numOfGameplays;
    /**
     * Represents the average time in minutes of gameplay in given period
     */
    private double avgTimeOfGameplay;
    /**
     * Represents the number of different board games played in given period
     */
    private int numOfDifferentBoardGames;
    /**
     * Represents the amount of gameplays and avg gameplay time per board game in given period
     */
    private List<BoardGameGameplaysStatsDto> boardGamesGameplaysStatsList;
    /**
     * Represents a percentage amount of gameplays per board game in given period
     */
    private Map<Long, Integer> percentageAmountOfGameplaysPerBoardGame;

}
