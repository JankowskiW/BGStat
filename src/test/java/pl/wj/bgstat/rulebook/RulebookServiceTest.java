package pl.wj.bgstat.rulebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RulebookServiceTest {

    /*
     * 1) Add rulebook in specific language if rulebook not exists
     * 2) Replace rulebook in specific language if rulebook already exists
     * 3) Delete existing rulebook
     * 4) Return rulebook in specific language
     */

    @Mock
    private RulebookRepository rulebookRepository;
    @InjectMocks
    private RulebookService rulebookService;

    @Test
    @DisplayName("Should add rulebook in specific language when rulebook not exists")
    void shouldAddRulebookInSpecificLanguageWhenRulebookNotExists() {
        // given
        long boardGameId = 1L;


        // when

        // then
    }

}