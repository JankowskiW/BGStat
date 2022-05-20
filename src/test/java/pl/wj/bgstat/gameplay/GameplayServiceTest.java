package pl.wj.bgstat.gameplay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.gameplay.GameplayServiceTestHelper.createGameplaysStatsDto;

@ExtendWith(MockitoExtension.class)
public class GameplayServiceTest {

    @Mock
    private GameplayRepository gameplayRepository;
    @InjectMocks
    private GameplayService gameplayService;

    @Test
    @DisplayName("Should return statistics about gameplays in given period")
    void shouldReturnStatsAboutGameplaysInGivenPeriod() {
        // given
        LocalDate fromDate = LocalDate.of(2021, Month.JANUARY, 1);
        LocalDate toDate = LocalDate.of(2021, Month.DECEMBER,31);
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

}
