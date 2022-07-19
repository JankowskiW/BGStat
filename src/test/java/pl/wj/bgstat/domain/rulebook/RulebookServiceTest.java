package pl.wj.bgstat.domain.rulebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.domain.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.RequestEnumException;
import pl.wj.bgstat.exception.RequestFileException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.domain.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.domain.rulebook.model.Rulebook;
import pl.wj.bgstat.domain.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.domain.rulebook.model.dto.RulebookResponseDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;
import static pl.wj.bgstat.domain.rulebook.model.RulebookMapper.mapToRulebook;
import static pl.wj.bgstat.domain.rulebook.model.RulebookMapper.mapToRulebookResponseDto;

@ExtendWith(MockitoExtension.class)
class RulebookServiceTest {

    private static final String RULEBOOKS_PATH = "//localhost/resources/rulebooks";

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private RulebookRepository rulebookRepository;
    @InjectMocks
    private RulebookService rulebookService;


    @Test
    @DisplayName("Should add rulebook in given language when board game exists and rulebook does not exist")
    void shouldAddRulebookInSpecificLanguageWhenBoardGameExistsRulebookNotExists() throws IOException {
        // given
        long boardGameId = 1L;
        long rulebookId = 2L;
        LanguageISO language = LanguageISO.PL;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, language);
        String path = String.format("%s/%d/%d_%s.pdf", RULEBOOKS_PATH, boardGameId, boardGameId, language);
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
        RulebookResponseDto rulebookResponseDto = rulebookService.addRulebook(rulebookRequestDto, multipartFile);

        // TODO: 25.06.2022 Find out how to test file creation without create it in real environment (try ReflectionTestUtils)
        Path tmpPath = Paths.get(String.format("%s/%d", RULEBOOKS_PATH, boardGameId));
        if(Files.exists(tmpPath)) {
            Files.walk(tmpPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        // then
        assertThat(rulebookResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw RequestFileException when IOException was thrown in addRulebook method")
    void shouldThrowExceptionWhenIOExceptionWasThrownInAddRulebookMethod() throws IOException {
        // given
        long boardGameId = 1L;
        LanguageISO language = LanguageISO.PL;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, language);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(rulebookRepository.existsByBoardGameIdAndLanguageIso(anyLong(), any(LanguageISO.class))).willReturn(false);
        willThrow(IOException.class).given(multipartFile).transferTo(any(File.class));

        // when
        assertThatThrownBy(() -> rulebookService.addRulebook(rulebookRequestDto, multipartFile))
                .isInstanceOf(RequestFileException.class)
                .hasMessage(createRequestFileExceptionSaveFailedMessage(multipartFile.getName()));

        // then
        verify(boardGameRepository).existsById(boardGameId);
        verify(rulebookRepository).existsByBoardGameIdAndLanguageIso(boardGameId, language);
    }

    @Test
    @DisplayName("Should throw RequestEnumException when given language iso code is null")
    void shouldThrowExceptionWhenGivenLanguageIsoCodeIsNull() {
        // given
        long boardGameId = 1L;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, null);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> rulebookService.addRulebook(rulebookRequestDto, multipartFile))
                .isInstanceOf(RequestEnumException.class)
                .hasMessage(createRequestEnumExceptionMessage("languageIso",
                        Arrays.stream(LanguageISO.values()).map(Enum::toString).collect(Collectors.toList())));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when rulebook exists by board game id and language")
    void shouldThrowExceptionWhenRulebookExistsByBoardGameIdAndLanguage() {
        // given
        long boardGameId = 1L;
        RulebookRequestDto rulebookRequestDto = new RulebookRequestDto(boardGameId, LanguageISO.EN);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(rulebookRepository.existsByBoardGameIdAndLanguageIso(anyLong(), any(LanguageISO.class))).willReturn(true);

        // when
        assertThatThrownBy(() -> rulebookService.addRulebook(rulebookRequestDto, multipartFile))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(RULEBOOK_RESOURCE_NAME, Optional.empty()));
    }

    @Test
    @DisplayName("Should replace rulebook file when rulebook exists")
    void shouldReplaceRulebookFileWhenRulebookExists() throws IOException {
        // given
        long rulebookId = 1L;
        long boardGameId = 2L;
        LanguageISO language = LanguageISO.PL;
        Rulebook rulebook = new Rulebook();
        rulebook.setId(rulebookId);
        rulebook.setBoardGameId(boardGameId);
        rulebook.setLanguageIso(language);
        rulebook.setPath(String.format("%s/%d/%d_%s.pdf", RULEBOOKS_PATH, boardGameId, boardGameId, language));
        RulebookResponseDto expectedResponse = mapToRulebookResponseDto(rulebook);
        given(rulebookRepository.findById(anyLong())).willReturn(Optional.of(rulebook));
        willDoNothing().given(multipartFile).transferTo(any(File.class));

        // when
        RulebookResponseDto rulebookResponseDto = rulebookService.editRulebook(rulebookId, multipartFile);

        // TODO: 25.06.2022 Find out how to test file creation without create it in real environment (try ReflectionTestUtils)
        Path tmpPath = Paths.get(String.format("%s/%d", RULEBOOKS_PATH, boardGameId));
        if(Files.exists(tmpPath)) {
            Files.walk(tmpPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        // then
        verify(multipartFile).transferTo(any(File.class));
        assertThat(rulebookResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw RequestFileException when IOException was thrown in editRulebook method")
    void shouldThrowExceptionWhenIOExceptionWasThrownInEditRulebookMethod() throws IOException {
        // given
        long rulebookId = 1L;
        long boardGameId = 2L;
        LanguageISO language = LanguageISO.PL;
        Rulebook rulebook = new Rulebook();
        rulebook.setPath(RULEBOOKS_PATH + String.format("/%d/%d_%s.pdf", boardGameId, boardGameId, language));
        given(rulebookRepository.findById(anyLong())).willReturn(Optional.of(rulebook));
        willThrow(IOException.class).given(multipartFile).transferTo(any(File.class));

        // when
        assertThatThrownBy(() -> rulebookService.editRulebook(rulebookId, multipartFile))
                .isInstanceOf(RequestFileException.class)
                .hasMessage(createRequestFileExceptionSaveFailedMessage(multipartFile.getName()));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when rulebook does not exist in database")
    void shouldThrowExceptionWhenRulebookDoesNotExists() {
        // given
        long rulebookId = 100L;

        // when
        assertThatThrownBy(() -> rulebookService.editRulebook(rulebookId, multipartFile))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(RULEBOOK_RESOURCE_NAME, ID_FIELD, rulebookId));
    }

    @Test
    @DisplayName("Should remove rulebook by id when exists")
    void shouldRemoveRulebookByIdWhenExists() {
        // given
        long rulebookId = 1L;
        long boardGameId = 2L;
        LanguageISO languageISO = LanguageISO.EN;
        Rulebook rulebook = new Rulebook();
        rulebook.setId(rulebookId);
        rulebook.setBoardGameId(boardGameId);
        rulebook.setLanguageIso(languageISO);
        rulebook.setPath(String.format("%s/%d/%d_%s.pdf", RULEBOOKS_PATH, boardGameId, boardGameId, languageISO));
        given(rulebookRepository.findById(anyLong())).willReturn(Optional.of(rulebook));
        willDoNothing().given(rulebookRepository).deleteById(anyLong());

        // when
        rulebookService.deleteRulebook(rulebookId);

        // then
        verify(rulebookRepository).deleteById(rulebookId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when rulebook does not exist in database")
    void shouldThrowExceptionWhenRulebookDoesNotExistInDatabase() {
        // given
        long rulebookId = 100L;
        given(rulebookRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> rulebookService.deleteRulebook(rulebookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(RULEBOOK_RESOURCE_NAME, ID_FIELD, rulebookId));
    }

    @Test
    @DisplayName("Should delete all rulebooks by board game id")
    void shouldDeleteAllRulebooksByBoardGameId() throws IOException {
        // given
        long boardGameId = 1L;
        given(boardGameRepository.existsById(anyLong())).willReturn(true);

        // when
        rulebookService.deleteAllRulebooksByBoardGameId(boardGameId);

        // then
        verify(rulebookRepository).deleteByBoardGameId(boardGameId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when board game does not exist in database")
    void shouldThrowExceptionWhenBoardGameDoesNotExistInDatabase() {
        // given
        long boardGameId = 100L;
        given(boardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> rulebookService.deleteAllRulebooksByBoardGameId(boardGameId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, boardGameId));
    }
}