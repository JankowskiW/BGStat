package pl.wj.bgstat.gameplay;

import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplayStatsDto;
import pl.wj.bgstat.gameplay.model.dto.GameplayRequestDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameplayServiceTestHelper {

    public static final long GAMEPLAY_DEFAULT_OBJECT_TYPE_ID = 4L;

    public static GameplaysStatsDto createGameplaysStatsDto(LocalDate fromDate, LocalDate toDate) {
        int numOfGameplays = 55;
        int numOfDifferentBoardGames = 5;
        List<BoardGameGameplayStatsDto> singleBoardGameGameplayStatsList = new ArrayList<>();
        Map<Long, Integer> percentageAmountOfGameplaysPerBoardGame = new HashMap<>();

        singleBoardGameGameplayStatsList.add(createBoardGameGameplayStatsDtoImpl(1, "Name 1", 1, 200));
        singleBoardGameGameplayStatsList.add(createBoardGameGameplayStatsDtoImpl(2, "Name 1", 4, 150));
        singleBoardGameGameplayStatsList.add(createBoardGameGameplayStatsDtoImpl(3, "Name 1", 9, 35));
        singleBoardGameGameplayStatsList.add(createBoardGameGameplayStatsDtoImpl(4, "Name 1", 16, 55));
        singleBoardGameGameplayStatsList.add(createBoardGameGameplayStatsDtoImpl(5, "Name 1", 25, 66));

        percentageAmountOfGameplaysPerBoardGame.put(1L, 2);
        percentageAmountOfGameplaysPerBoardGame.put(2L, 7);
        percentageAmountOfGameplaysPerBoardGame.put(3L, 16);
        percentageAmountOfGameplaysPerBoardGame.put(4L, 29);
        percentageAmountOfGameplaysPerBoardGame.put(5L, 46);

        return GameplaysStatsDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .numOfGameplays(numOfGameplays)
                .avgTimeOfGameplay(
                        singleBoardGameGameplayStatsList
                                .stream()
                                .mapToDouble(bg -> bg.getAvgTimeOfGameplay())
                                .sum())
                .numOfDifferentBoardGames(numOfDifferentBoardGames)
                .singleBoardGameGameplaysStatsList(singleBoardGameGameplayStatsList)
                .percentageAmountOfGameplaysPerBoardGame(percentageAmountOfGameplaysPerBoardGame)
                .build();
    }

    public static GameplaysStatsDto createEmptyGameplaysStatsDto(LocalDate fromDate, LocalDate toDate) {
        return GameplaysStatsDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .numOfGameplays(0)
                .avgTimeOfGameplay(0)
                .numOfDifferentBoardGames(0)
                .singleBoardGameGameplaysStatsList(new ArrayList<>())
                .percentageAmountOfGameplaysPerBoardGame(new HashMap<>())
                .build();
    }

    public static GameplayRequestDto createGameplayRequestDto() {
        return GameplayRequestDto.builder()
                .objectTypeId(GAMEPLAY_DEFAULT_OBJECT_TYPE_ID)
                .userId(1L)
                .boardGameId(1L)
                .userBoardGameId(1L)
                .comment("Comment")
                .startTime(LocalDateTime.of(2022,1,14,18,25))
                .endTime(LocalDateTime.of(2022,1,14,19,55))
                .playtime(65)  // playtime cannot be higher than difference between endTime and startTime but could be lower
                .build();
    }

    private static BoardGameGameplayStatsDto createBoardGameGameplayStatsDtoImpl(
            long bgId, String name, int numOfGp, double avgTimeOfGp) {
        return new BoardGameGameplayStatsDto() {
            @Override
            public long getBoardGameId() {
                return bgId;
            }

            @Override
            public String getBoardGameName() {
                return name;
            }

            @Override
            public int getNumOfGameplays() {
                return numOfGp;
            }

            @Override
            public double getAvgTimeOfGameplay() {
                return avgTimeOfGp;
            }
        };
    }
}