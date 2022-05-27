package pl.wj.bgstat.stats;

import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.stats.model.dto.StatsBoardGameGameplaysDto;
import pl.wj.bgstat.stats.model.dto.StatsGameplaysResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsServiceTestHelper {

    public static StatsGameplaysResponseDto createGameplaysStatsDto(LocalDate fromDate, LocalDate toDate) {
        int numOfGameplays = 55;
        int numOfBoardGames = 5;
        List<StatsBoardGameGameplaysDto> statsBoardGameGameplaysList = new ArrayList<>();
        Map<Long, Integer> percentageAmountOfGameplaysPerBoardGame = new HashMap<>();

        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(1, "Name 1", 1, 100));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(2, "Name 1", 4, 200));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(3, "Name 1", 9, 300));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(4, "Name 1", 16, 400));
        statsBoardGameGameplaysList.add(new StatsBoardGameGameplaysDto(5, "Name 1", 25, 500));
        percentageAmountOfGameplaysPerBoardGame.put(1L, 2);
        percentageAmountOfGameplaysPerBoardGame.put(2L, 7);
        percentageAmountOfGameplaysPerBoardGame.put(3L, 16);
        percentageAmountOfGameplaysPerBoardGame.put(4L, 29);
        percentageAmountOfGameplaysPerBoardGame.put(5L, 46);

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
                .percentageAmountOfGameplaysPerBoardGame(new HashMap<>())
                .build();
    }
}
