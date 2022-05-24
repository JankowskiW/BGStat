package pl.wj.bgstat.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplayStatsDto;
import pl.wj.bgstat.gameplay.model.Gameplay;
import pl.wj.bgstat.gameplay.model.GameplayMapper;
import pl.wj.bgstat.gameplay.model.dto.GameplayRequestDto;
import pl.wj.bgstat.gameplay.model.dto.GameplayResponseDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.user.UserRepository;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static pl.wj.bgstat.exception.ExceptionHelper.throwExceptionWhenNotExistsById;

@Service
@RequiredArgsConstructor
public class GameplayService {

    private static final long GAMEPLAY_DEFAULT_OBJECT_TYPE_ID = 4L;

    private final GameplayRepository gameplayRepository;
    private final BoardGameRepository boardGameRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final UserRepository userRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;

    public GameplaysStatsDto getGameplayStats(LocalDate fromDate, LocalDate toDate) {
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
                        .mapToDouble(BoardGameGameplayStatsDto::getAvgTimeOfGameplay)
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

    public GameplayResponseDto addGameplay(GameplayRequestDto gameplayRequestDto) {
        gameplayRequestDto.setObjectTypeId(validateSystemObjectTypeId(gameplayRequestDto.getObjectTypeId()));
        throwExceptionWhenNotExistsById(gameplayRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getUserId(), userRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getUserBoardGameId(), userBoardGameRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getBoardGameId(), boardGameRepository);
        Gameplay gameplay = GameplayMapper.mapToGameplay(gameplayRequestDto);
        gameplayRepository.save(gameplay);
        return GameplayMapper.mapToGameplayResponseDto(gameplay);
    }

    public GameplayResponseDto editGameplay(long id, GameplayRequestDto gameplayRequestDto) {
        gameplayRequestDto.setObjectTypeId(validateSystemObjectTypeId(gameplayRequestDto.getObjectTypeId()));
        throwExceptionWhenNotExistsById(id, gameplayRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getUserId(), userRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getUserBoardGameId(), userBoardGameRepository);
        throwExceptionWhenNotExistsById(gameplayRequestDto.getBoardGameId(), boardGameRepository);

        Gameplay gameplay = GameplayMapper.mapToGameplay(id, gameplayRequestDto);
        gameplayRepository.save(gameplay);
        return GameplayMapper.mapToGameplayResponseDto(gameplay);
    }

    public void deleteGameplay(long id) {
        throwExceptionWhenNotExistsById(id, gameplayRepository);
        gameplayRepository.deleteById(id);
    }

    private long validateSystemObjectTypeId(long id) {
        return id == 0 ? GAMEPLAY_DEFAULT_OBJECT_TYPE_ID : id;
    }
}
