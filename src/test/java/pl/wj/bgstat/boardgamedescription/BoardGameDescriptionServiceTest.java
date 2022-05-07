package pl.wj.bgstat.boardgamedescription;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class BoardGameDescriptionServiceTest {

    @Mock
    private BoardGameDescriptionRepository boardGameDescriptionRepository;
    @InjectMocks
    private BoardGameDescriptionService boardGameDescriptionService;

    private static final int NUMBER_OF_ELEMENTS = 5;

    private List<BoardGameDescription> boardGameDescriptionList;

    @BeforeEach
    void setUp() {
        boardGameDescriptionList = BoardGameDescriptionServiceTestHelper
                .populateBoardGameDescriptionList(NUMBER_OF_ELEMENTS);
    }

    @Test
    @DisplayName("Should edit board game description when exists")
    void shouldEditBoardGameDescriptionWhenExists() {
        // given
        long id = 1L;
        BoardGameDescriptionRequestDto boardGameDescriptionRequestDto =
                BoardGameDescriptionRequestDto.builder().description("New board game description").build();
        given(boardGameDescriptionRepository.findById(anyLong())).willReturn(
          boardGameDescriptionList.stream().filter(bgd -> bgd.getBoardGameId() != id).findAny());
        given(boardGameDescriptionRepository.save(any(BoardGameDescription.class))).willAnswer(
                i -> {
                    BoardGameDescription bgd = i.getArgument(0, BoardGameDescription.class);
                    bgd.setBoardGameId(id);
                    return bgd;
                });

        // when
        BoardGameDescriptionResponseDto boardGameDescriptionResponseDto =
                boardGameDescriptionService.editBoardGameDescription(id, boardGameDescriptionRequestDto);

        // then
        assertThat(boardGameDescriptionResponseDto).isNotNull();
        assertThat(boardGameDescriptionResponseDto.getBoardGameId()).isEqualTo(id);
        assertThat(boardGameDescriptionResponseDto.getDescription())
                .isEqualTo(boardGameDescriptionRequestDto.getDescription());

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit description of a not existing board game")
    void shouldThrowExceptionWhenTryingToEditNotExistingBoardGameDescription() {
        // given
        long id = 100L;
        given(boardGameDescriptionRepository.findById(anyLong())).willReturn(
                boardGameDescriptionList.stream().filter(bgd -> bgd.getBoardGameId() == id).findAny());

        // when
        assertThatThrownBy(() -> boardGameDescriptionService
                .editBoardGameDescription(id, new BoardGameDescriptionRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_DESCRIPTION_RESOURCE_NAME, ID_FIELD, id));
    }
}