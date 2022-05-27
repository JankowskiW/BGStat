package pl.wj.bgstat.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.stats.model.dto.StatsGameplaysResponseDto;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.stats.StatsServiceTestHelper.createGameplaysStatsDto;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;
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


}