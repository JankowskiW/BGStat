package pl.wj.bgstat.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.stats.model.dto.StatsBoardGameGameplaysDto;
import pl.wj.bgstat.stats.model.dto.StatsGameplaysResponseDto;
import pl.wj.bgstat.user.UserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static pl.wj.bgstat.exception.ExceptionHelper.throwExceptionWhenNotExistsById;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;

    public StatsGameplaysResponseDto getGameplaysStats(LocalDate fromDate, LocalDate toDate) {
        StatsGameplaysResponseDto statsGameplaysResponseDto =
                StatsGameplaysResponseDto.builder()
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .statsBoardGameGameplaysList(new ArrayList<>())
                        .percentageAmountOfGameplaysPerBoardGame(new HashMap<>())
                        .build();

        return getStatsGameplaysResponseDto(statsGameplaysResponseDto, statsRepository.getStatsByGivenPeriod(fromDate, toDate));
    }

    public StatsGameplaysResponseDto getGameplaysStatsOfGivenUser(long userId, LocalDate fromDate, LocalDate toDate, Long boardGameId) {

        throwExceptionWhenNotExistsById(userId, userRepository);
        if (boardGameId != null)
            throwExceptionWhenNotExistsById(boardGameId, boardGameRepository);

        StatsGameplaysResponseDto statsGameplaysResponseDto =
                StatsGameplaysResponseDto.builder()
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .statsBoardGameGameplaysList(new ArrayList<>())
                        .percentageAmountOfGameplaysPerBoardGame(new HashMap<>())
                        .build();

        return boardGameId == null ?
                getStatsGameplaysResponseDto(statsGameplaysResponseDto,
                        statsRepository.getStatsByGivenPeriodAndByUserId(userId, fromDate, toDate)) :
                getStatsGameplaysResponseDto(statsGameplaysResponseDto,
                        statsRepository.getStatsByGivenPeriodAndByUserIdAndByBoardGameId(userId, fromDate, toDate, boardGameId));
    }

    private StatsGameplaysResponseDto getStatsGameplaysResponseDto(
            StatsGameplaysResponseDto statsGameplaysResponseDto,
            List<StatsBoardGameGameplaysDto> statsBoardGameGameplaysList) {
        int numOfBg;
        int numOfGp;
        double avgTimeOfGp;
        double pAmount;
        int pDiff;
        int pSum = 0;
        numOfBg = statsBoardGameGameplaysList.size();

        if (numOfBg == 0) {
            statsGameplaysResponseDto.setNumOfGameplays(0);
            statsGameplaysResponseDto.setAvgTimeOfGameplay(0);
            statsGameplaysResponseDto.setNumOfGameplays(0);
            return statsGameplaysResponseDto;
        }

        numOfGp = statsBoardGameGameplaysList.stream()
                .mapToInt(StatsBoardGameGameplaysDto::getNumOfGameplays).sum();
        avgTimeOfGp = statsBoardGameGameplaysList.stream()
                .mapToDouble(StatsBoardGameGameplaysDto::getAvgTimeOfGameplay).average().getAsDouble();

        statsGameplaysResponseDto.setNumOfGameplays(numOfGp);
        statsGameplaysResponseDto.setAvgTimeOfGameplay(avgTimeOfGp);
        statsGameplaysResponseDto.setNumOfBoardGames(numOfBg);
        statsGameplaysResponseDto.setStatsBoardGameGameplaysList(statsBoardGameGameplaysList);

        Map<Long, Double> percentageAmountOfGameplaysPerBoardGame = new HashMap<>();

        for (StatsBoardGameGameplaysDto sbgg : statsBoardGameGameplaysList) {
            pAmount = (100.0 * sbgg.getNumOfGameplays()/numOfGp);
            pSum += (int) pAmount;
            percentageAmountOfGameplaysPerBoardGame.put(sbgg.getBoardGameId(), pAmount);
        }

        pDiff = Math.abs(100 - pSum);

        List<Long> sortedMapKeys = percentageAmountOfGameplaysPerBoardGame.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble((Double d) -> (d - Math.floor(d))).reversed()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (int i = 0; i < numOfBg; i++) {
            long k = sortedMapKeys.get(i);
            int v = (int)Math.floor(percentageAmountOfGameplaysPerBoardGame.get(k));
            v = i < pDiff ? v + 1 : v;
            statsGameplaysResponseDto.getPercentageAmountOfGameplaysPerBoardGame().put(k,v);
        }

        return statsGameplaysResponseDto;
    }
}
