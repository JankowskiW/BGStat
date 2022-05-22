package pl.wj.bgstat.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.boardgame.BoardGameRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
        GameplaysStatsDto gameplaysStatsDto = gameplayService.getGameplayActivity(fromDate, toDate);

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
        GameplaysStatsDto gameplaysStatsDto = gameplayService.getGameplayActivity(fromDate, toDate);

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

}
