package pl.wj.bgstat.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.gameplay.model.Gameplay;
import pl.wj.bgstat.gameplay.model.GameplayMapper;
import pl.wj.bgstat.gameplay.model.dto.GameplayRequestDto;
import pl.wj.bgstat.gameplay.model.dto.GameplayResponseDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.user.UserRepository;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;
import static pl.wj.bgstat.gameplay.GameplayServiceTestHelper.*;

@ExtendWith(MockitoExtension.class)
public class GameplayServiceTest {

    @Mock
    private GameplayRepository gameplayRepository;
    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @InjectMocks
    private GameplayService gameplayService;

    private LocalDate fromDate;
    private LocalDate toDate;

    @BeforeEach
    void setUp() {
        fromDate = LocalDate.of(2021, Month.JANUARY, 1);
        toDate = LocalDate.of(2021, Month.DECEMBER,31);
    }

    @Test
    @DisplayName("Should return statistics about gameplays in given period")
    void shouldReturnStatsAboutGameplaysInGivenPeriod() {
        // given
        GameplaysStatsDto expectedResponse = createGameplaysStatsDto(fromDate, toDate);
        given(gameplayRepository.getStatsByGivenPeriod(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(expectedResponse.getSingleBoardGameGameplaysStatsList());

        // when
        GameplaysStatsDto gameplaysStatsDto = gameplayService.getGameplayStats(fromDate, toDate);

        // then
        assertThat(gameplaysStatsDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return empty stats when there is no gameplays in given period")
    void shouldReturnEmptyStatsWhenThereIsNoGameplaysInGivenPeriod() {
        // given
        GameplaysStatsDto expectedResponse = createEmptyGameplaysStatsDto(fromDate, toDate);
        given(gameplayRepository.getStatsByGivenPeriod(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(new ArrayList<>());

        // when
        GameplaysStatsDto gameplaysStatsDto = gameplayService.getGameplayStats(fromDate, toDate);

        // then
        assertThat(gameplaysStatsDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return created gameplay")
    void shouldReturnCreatedGameplay() {
        // given
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        Gameplay gameplay = GameplayMapper.mapToGameplay(gameplayRequestDto);
        gameplay.setId(1L);
        GameplayResponseDto expectedResponse = GameplayMapper.mapToGameplayResponseDto(gameplay);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.save(any(Gameplay.class))).willAnswer(
                i -> {
                    Gameplay gp = i.getArgument(0, Gameplay.class);
                    gp.setId(gameplay.getId());
                    return gp;
                });

        // when
        GameplayResponseDto gameplayResponseDto = gameplayService.addGameplay(gameplayRequestDto);

        // then
        assertThat(gameplayResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return created gameplay with default system object type id")
    void shouldReturnCreatedGameplayWithDefaultSystemObjectTypeIdWhenObjectTypeNotSet() {
        // given
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setObjectTypeId(0L);
        Gameplay gameplay = GameplayMapper.mapToGameplay(gameplayRequestDto);
        gameplay.setId(1L);
        GameplayResponseDto expectedResponse = GameplayMapper.mapToGameplayResponseDto(gameplay);
        expectedResponse.setObjectTypeId(GAMEPLAY_DEFAULT_OBJECT_TYPE_ID);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.save(any(Gameplay.class))).willAnswer(
                i -> {
                    Gameplay gp = i.getArgument(0, Gameplay.class);
                    gp.setId(gameplay.getId());
                    return gp;
                });

        // when
        GameplayResponseDto gameplayResponseDto = gameplayService.addGameplay(gameplayRequestDto);

        // then
        assertThat(gameplayResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when SystemObjectType id does not exist in database")
    void shouldThrowExceptionWhenSystemObjectTypeIdDoesNotExist() {
        // given
        long systemObjectTypeId = 99L;
        GameplayRequestDto gameplayRequestDto = new GameplayRequestDto();
        gameplayRequestDto.setObjectTypeId(systemObjectTypeId);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.addGameplay(gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, systemObjectTypeId));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User id does not exist in database")
    void shouldThrowExceptionWhenUserIdDoesNotExist() {
        // given
        long userId = 99L;
        GameplayRequestDto gameplayRequestDto = new GameplayRequestDto();
        gameplayRequestDto.setUserId(userId);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.addGameplay(gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_RESOURCE_NAME, ID_FIELD, userId));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when UserBoardGame id does not exist in database")
    void shouldThrowExceptionWhenUserBoardGameIdDoesNotExist() {
        // given
        long userBoardGameId = 99L;
        GameplayRequestDto gameplayRequestDto = new GameplayRequestDto();
        gameplayRequestDto.setUserBoardGameId(userBoardGameId);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.addGameplay(gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, userBoardGameId));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when BoardGame id does not exist in database")
    void shouldThrowExceptionWhenBoardGameIdDoesNotExist() {
        // given
        long boardGameId = 99L;
        GameplayRequestDto gameplayRequestDto = new GameplayRequestDto();
        gameplayRequestDto.setBoardGameId(boardGameId);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.addGameplay(gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, boardGameId));
    }

    @Test
    @DisplayName("Should edit gameplay when exists")
    void shouldEditGameplayWhenExists() {
        // given
        long id = 1L;
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setUserBoardGameId(2L);
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.save(any(Gameplay.class))).willAnswer(
                i -> {
                   Gameplay gp = i.getArgument(0, Gameplay.class);
                   gp.setId(id);
                   return gp;
                });

        // when
        GameplayResponseDto gameplayResponseDto = gameplayService.editGameplay(id, gameplayRequestDto);

        // then
        assertThat(gameplayResponseDto).isNotNull();
        assertThat(gameplayResponseDto.getId()).isEqualTo(id);
        assertThat(gameplayResponseDto.getUserBoardGameId()).isEqualTo(gameplayRequestDto.getUserBoardGameId());
    }

    @Test
    @DisplayName("Should set default system object type id when id is set to zero")
    void shouldSetDefaultSystemObjectTypeIdWhenIdIsSetToZero() {
        // given
        long id = 1L;
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setObjectTypeId(0L);
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.save(any(Gameplay.class))).willAnswer(
                i -> {
                    Gameplay gp = i.getArgument(0, Gameplay.class);
                    gp.setId(id);
                    return gp;
                });

        // when
        GameplayResponseDto gameplayResponseDto = gameplayService.editGameplay(id, gameplayRequestDto);

        // then
        assertThat(gameplayResponseDto).isNotNull();
        assertThat(gameplayResponseDto.getId()).isEqualTo(id);
        assertThat(gameplayResponseDto.getUserBoardGameId()).isEqualTo(gameplayRequestDto.getUserBoardGameId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when Gameplay id does not exist in database")
    void shouldThrowExceptionWhenGameplayIdDoesNotExist() {
        // given
        long id = 99L;
        given(gameplayRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.editGameplay(id, new GameplayRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(GAMEPLAY_RESOURCE_NAME, ID_FIELD, id));

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when SystemObjectType id of gameplay does not exist in database")
    void shouldThrowExceptionWhenSystemObjectTypeIdOfGameplayDoesNotExist() {
        // given
        long systemObjectTypeId = 99L;
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setObjectTypeId(systemObjectTypeId);
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.editGameplay(1L, gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, systemObjectTypeId));

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User id of gameplay does not exist in database")
    void shouldThrowExceptionWhenUserIdOfGameplayDoesNotExist() {
        // given
        long userId = 99L;
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setUserId(userId);
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.editGameplay(1L, gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_RESOURCE_NAME, ID_FIELD, userId));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when UserBoardGame id of gameplay does not exist in database")
    void shouldThrowExceptionWhenUserBoardGameIdOfGameplayDoesNotExist() {
        // given
        long userBoardGameId = 99L;
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setUserBoardGameId(userBoardGameId);
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.editGameplay(1L, gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, userBoardGameId));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when BoardGame id of gameplay does not exist in database")
    void shouldThrowExceptionWhenBoardGameIdOfGameplayDoesNotExist() {
        // given
        long boardGameId = 99L;
        GameplayRequestDto gameplayRequestDto = createGameplayRequestDto();
        gameplayRequestDto.setBoardGameId(boardGameId);
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.editGameplay(1L, gameplayRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, boardGameId));
    }

    @Test
    @DisplayName("Should remove gameplay when exists in database")
    void shouldRemoveGameplayByIdWhenExists() {
        // given
        long id = 1L;
        given(gameplayRepository.existsById(anyLong())).willReturn(true);
        willDoNothing().given(gameplayRepository).deleteById(anyLong());

        // when
        gameplayService.deleteGameplay(id);

        // then
        verify(gameplayRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing gameplay")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingGameplay() {
        // given
        long id = 100L;
        given(gameplayRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> gameplayService.deleteGameplay(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(GAMEPLAY_RESOURCE_NAME, ID_FIELD, id));
    }
}
