package pl.wj.bgstat.gameplay;

import com.fasterxml.jackson.core.util.InternCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplayStatsDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameplayService {

    private final GameplayRepository gameplayRepository;

    public GameplaysStatsDto getGameplayActivity(LocalDate fromDate, LocalDate toDate) {
        List<BoardGameGameplayStatsDto> singleBoardGameGameplaysStatsList =
            gameplayRepository.getStatsByGivenPeriod(fromDate, toDate);

        GameplaysStatsDto gameplaysStatsDto = new GameplaysStatsDto();
        gameplaysStatsDto.setFromDate(fromDate);
        gameplaysStatsDto.setToDate(toDate);
        gameplaysStatsDto.setSingleBoardGameGameplaysStatsList(new ArrayList<>());
        gameplaysStatsDto.setPercentageAmountOfGameplaysPerBoardGame(new HashMap<>());

        if (singleBoardGameGameplaysStatsList.size() == 0) {
            gameplaysStatsDto.setNumOfGameplays(0);
            gameplaysStatsDto.setAvgTimeOfGameplay(0);
            gameplaysStatsDto.setNumOfDifferentBoardGames(0);
            return gameplaysStatsDto;
        }

        int numOfBg = singleBoardGameGameplaysStatsList.size();
        int numOfGp =  singleBoardGameGameplaysStatsList.stream()
                .mapToInt(BoardGameGameplayStatsDto::getNumOfGameplays)
                .sum();

        gameplaysStatsDto.setNumOfGameplays(numOfGp);
        gameplaysStatsDto.setAvgTimeOfGameplay(
                singleBoardGameGameplaysStatsList.stream()
                        .mapToInt(BoardGameGameplayStatsDto::getAvgTimeOfGameplay)
                        .sum());
        gameplaysStatsDto.setNumOfDifferentBoardGames(numOfBg);
        gameplaysStatsDto.setSingleBoardGameGameplaysStatsList(singleBoardGameGameplaysStatsList);

        Map<Long, Double> percentageAmountOfGameplayserPerBoardGame = new HashMap<>();
        double pAmount;
        int pSum = 0;

        for (BoardGameGameplayStatsDto bggs : singleBoardGameGameplaysStatsList) {
            pAmount = (100.0*bggs.getNumOfGameplays()/numOfGp);
            pSum += (int)pAmount;
            percentageAmountOfGameplayserPerBoardGame.put(bggs.getBoardGameId(), pAmount);
        }

        int pDiff = Math.abs(100 - pSum);

        List<Long> sortedMapKeys = percentageAmountOfGameplayserPerBoardGame.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble((Double d) -> (d - Math.floor(d))).reversed()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (int i = 0; i < numOfBg; i++) {
            long k = sortedMapKeys.get(i);
            int v;
            if (i < pDiff) {
                v = (int)Math.floor(percentageAmountOfGameplayserPerBoardGame.get(sortedMapKeys.get(i)))+1;
            } else
            {
                v = (int)Math.floor(percentageAmountOfGameplayserPerBoardGame.get(sortedMapKeys.get(i)));
            }
            gameplaysStatsDto.getPercentageAmountOfGameplaysPerBoardGame().put(k,v);
        }

        return  gameplaysStatsDto;
    }

}
