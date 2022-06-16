package pl.wj.bgstat.rulebook;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.ExceptionHelper;
import pl.wj.bgstat.exception.RequestEnumException;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.rulebook.model.Rulebook;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mockStatic;
import static pl.wj.bgstat.exception.ExceptionHelper.createRequestEnumExceptionMessage;
import static pl.wj.bgstat.rulebook.model.RulebookMapper.mapToRulebook;
import static pl.wj.bgstat.rulebook.model.RulebookMapper.mapToRulebookResponseDto;

@ExtendWith(MockitoExtension.class)
class RulebookServiceTest {
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private RulebookRepository rulebookRepository;
    @InjectMocks
    private RulebookService rulebookService;

    private static final String RULEBOOKS_PATH = "\\\\localhost\\resources\\rulebooks";

    private static MockedStatic ms;

    @BeforeAll
    static void beforeAll() {
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
        long rulebookId = 2L;
        LanguageISO language = LanguageISO.PL;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, language);
        String path = RULEBOOKS_PATH + String.format("\\%d\\%d_%s.pdf", boardGameId, boardGameId, language);
        Rulebook rulebook = mapToRulebook(rulebookRequestDto, path);
        rulebook.setId(rulebookId);
        RulebookResponseDto expectedResponse = mapToRulebookResponseDto(rulebook);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(rulebookRepository.existsByBoardGameIdAndLanguageIso(anyLong(), any(LanguageISO.class))).willReturn(false);
        willDoNothing().given(multipartFile).transferTo(any(File.class));
        given(rulebookRepository.save(any(Rulebook.class))).willAnswer(
                i -> {
                    Rulebook rb = i.getArgument(0, Rulebook.class);
                    rb.setId(rulebook.getId());
                    return rb;
                });

        // when
        RulebookResponseDto rulebookResponseDto = rulebookService.addOrReplaceRulebook(rulebookRequestDto, multipartFile);

        // then
        assertThat(rulebookResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw RequestEnumException when given language iso code is null")
    void shouldThrowRequestEnumExceptionWhenGivenLanguageIsoCodeIsNull() {
        // given
        long boardGameId = 1L;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, null);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> rulebookService.addOrReplaceRulebook(rulebookRequestDto, multipartFile))
                .isInstanceOf(RequestEnumException.class)
                .hasMessage(createRequestEnumExceptionMessage("languageIso",
                        Arrays.stream(LanguageISO.values()).map(Enum::toString).collect(Collectors.toList())));
    }
}