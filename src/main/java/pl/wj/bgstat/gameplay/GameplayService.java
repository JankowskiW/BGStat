package pl.wj.bgstat.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
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
