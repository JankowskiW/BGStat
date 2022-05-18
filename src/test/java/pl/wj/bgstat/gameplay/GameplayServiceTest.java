package pl.wj.bgstat.gameplay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GameplayServiceTest {

    @Mock
    private GameplayRepository gameplayRepository;
    @InjectMocks
    private GameplayService gameplayService;

    @Test
    @DisplayName("Should return number of gameplays in given period")
    void shouldReturnNumberOfGameplaysInGivenPeriod() {
        // given
        long expectedNumberOfGameplays = 10L;
        LocalDate fromDate = LocalDate.of(2021, Month.JANUARY, 1);
        LocalDate toDate = LocalDate.of(2021, Month.DECEMBER,31);
        given(gameplayRepository.countInGivenPeriod(
                any(LocalDate.class), any(LocalDate.class))).willReturn(expectedNumberOfGameplays);

        // when
        long numberOfGameplays =  gameplayService.getGameplayActivity(fromDate, toDate);

        // then
        assertThat(numberOfGameplays).isEqualTo(expectedNumberOfGameplays);
    }

}
