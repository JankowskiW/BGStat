package pl.wj.bgstat.domain.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.domain.boardgame.BoardGameRepository;
import pl.wj.bgstat.domain.stats.model.dto.GameplaysPercentageAmountDto;
import pl.wj.bgstat.domain.stats.model.dto.StatsBoardGameGameplaysDto;
import pl.wj.bgstat.domain.stats.model.dto.StatsGameplaysResponseDto;
import pl.wj.bgstat.domain.user.UserRepository;

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
                        .percentageAmountOfGameplaysPerBoardGame(new ArrayList<>())
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
                        .percentageAmountOfGameplaysPerBoardGame(new ArrayList<>())
                        .build();

        return boardGameId == null ?
                getStatsGameplaysResponseDto(statsGameplaysResponseDto,
                        statsRepository.getStatsByGivenPeriodAndByUserId(userId, fromDate, toDate)) :
                getStatsGameplaysResponseDto(statsGameplaysResponseDto,
                        statsRepository.getStatsByGivenPeriodAndByUserIdAndByBoardGameId(userId, fromDate, toDate, boardGameId));
    }

    public StatsGameplaysResponseDto getGameplaysStatsOfGivenBoardGame(long boardGameId, LocalDate fromDate, LocalDate toDate) {
        throwExceptionWhenNotExistsById(boardGameId, boardGameRepository);

        StatsGameplaysResponseDto statsGameplaysResponseDto =
                StatsGameplaysResponseDto.builder()
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .statsBoardGameGameplaysList(new ArrayList<>())
                        .percentageAmountOfGameplaysPerBoardGame(new ArrayList<>())
                        .build();

        return getStatsGameplaysResponseDto(statsGameplaysResponseDto,
                statsRepository.getStatsByGivenPeriodAndByBoardGameId(boardGameId, fromDate, toDate));
    }

    private StatsGameplaysResponseDto getStatsGameplaysResponseDto(
            StatsGameplaysResponseDto statsGameplaysResponseDto,
            List<StatsBoardGameGameplaysDto> statsBoardGameGameplaysList) {
        int numOfBg;
        int numOfGp;
        double avgTimeOfGp;
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

        List<GameplaysPercentageAmountDto> percentageAmountOfGameplaysPerBoardGame =
                statsBoardGameGameplaysList
                .stream()
                .map(sbgg ->new GameplaysPercentageAmountDto(sbgg.getBoardGameId(), (100.0 * sbgg.getNumOfGameplays()/numOfGp)))
                .sorted(Comparator.comparing(GameplaysPercentageAmountDto::getFracPercAmount).reversed())
                .collect(Collectors.toList());

        pSum = percentageAmountOfGameplaysPerBoardGame.stream().mapToInt(pa -> (int)pa.getPercentageAmount()).sum();

        pDiff = Math.abs(100 - pSum);

        percentageAmountOfGameplaysPerBoardGame.stream().forEach(pa -> pa.setPercentageAmount(Math.floor(pa.getPercentageAmount())));

        for (int i = 0; i < pDiff; i++) {
            GameplaysPercentageAmountDto pa = percentageAmountOfGameplaysPerBoardGame.get(i);
            pa.setPercentageAmount(pa.getPercentageAmount()+1);
        }

        statsGameplaysResponseDto.setPercentageAmountOfGameplaysPerBoardGame(
                percentageAmountOfGameplaysPerBoardGame
                        .stream()
                        .sorted(Comparator.comparingDouble(GameplaysPercentageAmountDto::getPercentageAmount))
                        .collect(Collectors.toList()));

        return statsGameplaysResponseDto;
    }
}
