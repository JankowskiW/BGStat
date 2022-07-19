package pl.wj.bgstat.domain.stats;

import pl.wj.bgstat.domain.stats.model.dto.GameplaysPercentageAmountDto;
import pl.wj.bgstat.domain.stats.model.dto.StatsBoardGameGameplaysDto;
import pl.wj.bgstat.domain.stats.model.dto.StatsGameplaysResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatsServiceTestHelper {

    public static StatsGameplaysResponseDto createGameplaysStatsDto(LocalDate fromDate, LocalDate toDate) {
        int numOfGameplays = 55;
        int numOfBoardGames = 5;
        List<StatsBoardGameGameplaysDto> statsBoardGameGameplaysList = new ArrayList<>();
        List<GameplaysPercentageAmountDto> percentageAmountOfGameplaysPerBoardGame = new ArrayList<>();

        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(1, "Name 1", 1, 100));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(2, "Name 1", 4, 200));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(3, "Name 1", 9, 300));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(4, "Name 1", 16, 400));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(5, "Name 1", 25, 500));
        percentageAmountOfGameplaysPerBoardGame.add(new GameplaysPercentageAmountDto(1L, 2));
        percentageAmountOfGameplaysPerBoardGame.add(new GameplaysPercentageAmountDto(2L, 7));
        percentageAmountOfGameplaysPerBoardGame.add(new GameplaysPercentageAmountDto(3L, 16));
        percentageAmountOfGameplaysPerBoardGame.add(new GameplaysPercentageAmountDto(4L, 29));
        percentageAmountOfGameplaysPerBoardGame.add(new GameplaysPercentageAmountDto(5L, 46));

        return StatsGameplaysResponseDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .numOfGameplays(numOfGameplays)
                .avgTimeOfGameplay(
                        statsBoardGameGameplaysList
                                .stream()
                                .mapToDouble(bg -> bg.getAvgTimeOfGameplay())
                                .average().getAsDouble())
                .numOfBoardGames(numOfBoardGames)
                .statsBoardGameGameplaysList(statsBoardGameGameplaysList)
                .percentageAmountOfGameplaysPerBoardGame(percentageAmountOfGameplaysPerBoardGame)
                .build();
    }

    public static StatsGameplaysResponseDto createEmptyStatsGameplaysResponseDto(LocalDate fromDate, LocalDate toDate) {
        return StatsGameplaysResponseDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .numOfGameplays(0)
                .avgTimeOfGameplay(0)
                .numOfBoardGames(0)
                .statsBoardGameGameplaysList(new ArrayList<>())
                .percentageAmountOfGameplaysPerBoardGame(new ArrayList<>())
                .build();
    }
}
