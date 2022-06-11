package pl.wj.bgstat.rulebook;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mockStatic;
import static pl.wj.bgstat.rulebook.RulebookServiceTestHelper.createMultipartRulebook;

@ExtendWith(MockitoExtension.class)
class RulebookServiceTest {

    /*
     * 1) Add rulebook in specific language if rulebook does not exist
     * 2) Replace rulebook in specific language if rulebook already exists
     * 3) Delete existing rulebook
     * 4) Return rulebook in specific language
     */

    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private RulebookRepository rulebookRepository;
    @InjectMocks
    private RulebookService rulebookService;

    private static final String RULEBOOKS_PATH = "\\\\localhost\\resources\\rulebooks";

    private static MockedStatic ms;
    private static MultipartFile okRulebookFile;
    private static MultipartFile notOkRulebookFile;


    @BeforeAll
    static void beforeAll() throws IOException {
        okRulebookFile = createMultipartRulebook(true);
        notOkRulebookFile = createMultipartRulebook(false);
        ms = mockStatic(ImageIO.class);
    }

    @AfterAll
    static void afterAll() {
        ms.close();
    }

    @Test
    @DisplayName("Should add rulebook in given language when board game exists and rulebook does not exist")
    void shouldAddRulebookInSpecificLanguageWhenBoardGameExistsRulebookNotExists() throws IOException {
        // given
        long boardGameId = 1L;
        LanguageISO language = LanguageISO.PL;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, language);
        String path = RULEBOOKS_PATH + String.format("\\%d\\%d_%S", boardGameId, boardGameId, language);
        MultipartFile file = createMultipartRulebook(true);

        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(rulebookRepository.existsByBoardGameIdAndLanguageIso(anyLong(), anyString())).willReturn(false);
        willDoNothing().given(file).transferTo(any(File.class));

        // when
        RulebookResponseDto rulebookResponseDto = rulebookService.addOrReplaceRulebook(rulebookRequestDto, okRulebookFile);

        // then
        assertThat(rulebookResponseDto).isNotNull();
        assertThat(rulebookResponseDto.getBoardGameId()).isEqualTo(boardGameId);
        assertThat(rulebookResponseDto.getPath()).isEqualTo(path);
    }

}