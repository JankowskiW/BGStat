package pl.wj.bgstat.stats.model.dto;

import lombok.*;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StatsGameplaysResponseDto {
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
    private int numOfBoardGames;
    /**
     * Represents stats of board game in given period
     */
    private List<StatsBoardGameGameplaysDto> statsBoardGameGameplaysList;
    /**
     * Represents a percentage amount of gameplays per board game in given period
     */
    private List<GameplaysPercentageAmountDto> percentageAmountOfGameplaysPerBoardGame;

}
