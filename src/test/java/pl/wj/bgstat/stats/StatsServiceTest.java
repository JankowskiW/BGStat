package pl.wj.bgstat.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.stats.model.dto.StatsGameplaysResponseDto;
import pl.wj.bgstat.user.UserRepository;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.stats.StatsServiceTestHelper.createEmptyStatsGameplaysResponseDto;
import static pl.wj.bgstat.stats.StatsServiceTestHelper.createGameplaysStatsDto;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardGameRepository boardGameRepository;
    @InjectMocks
    private StatsService statsService;

    private LocalDate fromDate;
    private LocalDate toDate;

    @BeforeEach
    void setUp() {
        fromDate = LocalDate.of(2021, Month.JANUARY, 1);
        toDate = LocalDate.of(2021, Month.DECEMBER,31);
    }

    @Test
    @DisplayName("Should return stats about gameplays in given period")
    void shouldReturnStatsAboutGameplaysInGivenPeriod() {
        // given
        StatsGameplaysResponseDto expectedResponse = createGameplaysStatsDto(fromDate, toDate);
        given(statsRepository.getStatsByGivenPeriod(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(expectedResponse.getStatsBoardGameGameplaysList());

        // when
        StatsGameplaysResponseDto statsGameplaysResponseDto = statsService.getGameplaysStats(fromDate, toDate);

        // then
        assertThat(statsGameplaysResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return empty stats if there is no gameplays in given period")
    void shouldReturnEmptyStatsIfThereIsNoGameplaysInGivenPeriod() {
        // given
        StatsGameplaysResponseDto expectedResponse = createEmptyStatsGameplaysResponseDto(fromDate, toDate);
        given(statsRepository.getStatsByGivenPeriod(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(expectedResponse.getStatsBoardGameGameplaysList());

        // when
        StatsGameplaysResponseDto statsGameplaysResponseDto = statsService.getGameplaysStats(fromDate, toDate);

        // then
        assertThat(statsGameplaysResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return stats about gameplays of specific user in given period")
    void shouldReturnStatsAboutGameplaysOfSpecificUserInGivenPeriod() {
        // given
        long userId = 1L;
        StatsGameplaysResponseDto expectedResponse = createGameplaysStatsDto(fromDate, toDate);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(statsRepository.getStatsByGivenPeriodAndByUserId(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(expectedResponse.getStatsBoardGameGameplaysList());

        // when
        StatsGameplaysResponseDto statsGameplaysResponseDto =
                statsService.getGameplaysStatsOfGivenUser(userId, fromDate, toDate, null);

        // then
        assertThat(statsGameplaysResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return stats about gameplays of specific user and board game in given period")
    void shouldReturnStatsAboutGameplaysOfSpecificUserAndBoardGameInGivenPeriod() {
        // given
        long userId = 1L;
        long boardGameId = 1L;
        StatsGameplaysResponseDto expectedResponse = createGameplaysStatsDto(fromDate, toDate);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(statsRepository.getStatsByGivenPeriodAndByUserIdAndByBoardGameId(
                anyLong(), any(LocalDate.class), any(LocalDate.class), anyLong()))
                .willReturn(expectedResponse.getStatsBoardGameGameplaysList());

        // when
        StatsGameplaysResponseDto statsGameplaysResponseDto =
                statsService.getGameplaysStatsOfGivenUser(userId, fromDate, toDate, boardGameId);

        // then
        assertThat(statsGameplaysResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

}