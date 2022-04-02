package pl.wj.bgstat.boardgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.boardgame.BoardGameServiceTestHelper.*;

@ExtendWith(MockitoExtension.class)
class BoardGameServiceTest {

    @Mock
    private BoardGameRepository boardGameRepository;
    @InjectMocks
    private BoardGameService boardGameService;

    private static final int PAGE_SIZE = 4;
    private static final int NUMBER_OF_ELEMENTS = 20;

    private List<BoardGame> boardGameList;
    private List<BoardGameHeaderDto> boardGameHeaderList;
    private BoardGameRequestDto boardGameRequestDto;

    @BeforeEach
    void setUp() {
        boardGameList = populateBoardGameList(NUMBER_OF_ELEMENTS);
        boardGameHeaderList = populateBoardGameHeaderDtoList(boardGameList);
        boardGameRequestDto = createBoardGameRequestDto(boardGameList.size());
    }

    @Test
    @DisplayName("Should return only one but not last page of board game headers")
    void shouldReturnOnlyOneButNotLastPageOfBoardGameHeaders() {
        // given
        int pageNumber = 2;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(boardGameHeaderList.subList(fromIndex, toIndex)));

        // when
        Page<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(boardGameHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(boardGameHeaderList.subList(fromIndex, toIndex));
    }

    @Test
    @DisplayName("Should return only last page of board game headers")
    void shouldReturnOnlyLastPageOfBoardGameHeaders() {
        // given
        int lastPageNumber = (int) ceil(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE);
        int lastPageSize = (NUMBER_OF_ELEMENTS - (int) floor(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE) * PAGE_SIZE);
        lastPageSize = lastPageSize == 0 ? PAGE_SIZE : lastPageSize;
        int fromIndex = NUMBER_OF_ELEMENTS - lastPageSize;
        int toIndex = NUMBER_OF_ELEMENTS;
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(boardGameHeaderList.subList(fromIndex, toIndex)));

        // when
        Page<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(PageRequest.of(lastPageNumber, PAGE_SIZE));

        // then
        assertThat(boardGameHeaders)
                .isNotNull()
                .hasSize(lastPageSize)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(boardGameHeaderList.subList(fromIndex, toIndex));
    }

    @Test
    @DisplayName("Should return empty list of board game headers when page number is too high ")
    void shouldReturnEmptyListOfBoardGameHeaders() {
        // given
        int tooHighPageNumber = (int) ceil(boardGameHeaderList.size() / (double) PAGE_SIZE) + 1;
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));

        // when
        Page<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(PageRequest.of(tooHighPageNumber, PAGE_SIZE));

        // then
        assertThat(boardGameHeaders)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("Should throw IllegalAccessError when findAllBoardGameHeaders repo method accessing database ")
    void shouldThrowErrorWhenFindAllHeadersAccessingDb() {
        // given
        int pageNumber = 2;
        String exMsg = "Database access error";
        willThrow(new IllegalAccessError(exMsg)).given(boardGameRepository).findAllBoardGameHeaders(any(Pageable.class));

        // when
        assertThatThrownBy(() -> boardGameService.getBoardGameHeaders(PageRequest.of(pageNumber, PAGE_SIZE)))
                .isInstanceOf(IllegalAccessError.class)
                .hasMessage(exMsg);
    }

    @Test
    @DisplayName("Should return only one board game details with description")
    void shouldReturnSingleBoardGameDetailsById() {
        // given
        long id = 1l;
        Optional<BoardGame> returnedBoardGame = boardGameList.stream()
                .filter(bg -> bg.getId() == id)
                .findAny();
        BoardGameResponseDto expectedResponse = BoardGameMapper.mapToBoardGameResponseDto(returnedBoardGame.orElseThrow());
        given(boardGameRepository.findWithDescriptionById(anyLong())).willReturn(returnedBoardGame);

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.getSingleBoardGame(id);

        // then
        assertThat(boardGameResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when cannot find board game by id")
    void shouldThrowExceptionWhenCannotFindBoardGameById() {
        // given
        long id = boardGameList.size() + 1;
        String exMsg = "No such board game with id: " + id;
        given(boardGameRepository.findWithDescriptionById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .findAny());

        // when
        assertThatThrownBy(() -> boardGameService.getSingleBoardGame(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
    }

    @Test
    @DisplayName("Should create and return created board game")
    void shouldReturnCreatedBoardGame() {
        // given
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGame.setId(boardGameList.size()+1);
        boardGame.getBoardGameDescription().setBoardGameId(boardGame.getId());
        BoardGameResponseDto expectedResponse = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        given(boardGameRepository.existsByName(anyString())).willReturn(
                boardGameList.stream()
                        .filter(bg -> bg.getName().equals(boardGameRequestDto.getName()))
                        .count() > 0);
        given(boardGameRepository.save(any(BoardGame.class))).willAnswer(
                i -> {
                    BoardGame bg = i.getArgument(0, BoardGame.class);
                    bg.setId(boardGame.getId());
                    bg.getBoardGameDescription().setBoardGameId(boardGame.getId());
                    return bg;
                });

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.addBoardGame(boardGameRequestDto);

        // then
        assertThat(boardGameResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw EntityExistsException when board game name already exists in database")
    void shouldThrowExceptionWhenBoardGameNameExists() {
        // given
        String boardGameName = "Name No. 1";
        String exMsg = "Board game with name '" +
                boardGameName + "' already exists in database";
        BoardGameRequestDto boardGameRequestDto = new BoardGameRequestDto(
                boardGameName, 1, 1, 5, 2, 150, "DESCRIPTION");
        given(boardGameRepository.existsByName(anyString()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getName().equals(boardGameName))
                        .count() > 0);

        // when
        assertThatThrownBy(() -> boardGameService.addBoardGame(boardGameRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(exMsg);
    }

    @Test
    @DisplayName("Should edit board game when exists")
    void shouldEditBoardGameWhenExists() {
        // given
        long id = 1l;
        BoardGame boardGame = boardGameList.stream().filter(bg -> bg.getId() == id).findFirst().orElseThrow();
        BoardGameRequestDto boardGameRequestDto = BoardGameRequestDto.builder()
                .name(boardGame.getName())
                .recommendedAge(boardGame.getRecommendedAge()*2)
                .build();
        given(boardGameRepository.existsById(anyLong())).willReturn(
                boardGameList.stream().filter(bg -> bg.getId() == id).count() > 0);
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                boardGameList.stream().filter(bg -> bg.getId() != id &&
                        bg.getName().equals(boardGameRequestDto.getName())).count() > 0);
        given(boardGameRepository.save(any(BoardGame.class))).willAnswer(
                i -> {
                    BoardGame bg = i.getArgument(0, BoardGame.class);
                    bg.setId(id);
                    return bg;
                });

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.editBoardGame(id, boardGameRequestDto);

        // then
        assertThat(boardGameResponseDto).isNotNull();
        assertThat(boardGameResponseDto.getId()).isEqualTo(id);
        assertThat(boardGameResponseDto.getRecommendedAge()).isEqualTo(boardGameRequestDto.getRecommendedAge());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to edit non existing board game")
    void shouldThrowExceptionWhenTryingToEditNonExistingBoardGame() {
        // given
        long id = 100l;
        String exMsg = "No such board game with id: " + id;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .count() > 0);

        // when
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, new BoardGameRequestDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
    }

    @Test
    @DisplayName("Should throw EntityExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1l;
        BoardGame boardGame = boardGameList.stream().filter(bg -> bg.getId() == id).findFirst().orElseThrow();
        BoardGameRequestDto boardGameRequestDto = new BoardGameRequestDto(
                boardGameList.get(1).getName(), boardGame.getRecommendedAge(), boardGame.getMinPlayersNumber(),
                boardGame.getMaxPlayersNumber(), boardGame.getComplexity(), boardGame.getPlayingTime(),
                boardGame.getBoardGameDescription().getDescription());
        String exMsg = "Board game with name '" + boardGameRequestDto.getName() + "' already exists in database";
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .count() > 0);
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() != id && bg.getName().equals(boardGameRequestDto.getName()))
                        .count() > 0);

        // when
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, boardGameRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(exMsg);
    }


    @Test
    @DisplayName("Should remove board game by id when id exists in database")
    void shouldRemoveBoardGameByIdWhenIdExists () {
        // given
        long id = 3l;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .count() > 0);
        willDoNothing().given(boardGameRepository).deleteById(anyLong());

        // when
        boardGameService.deleteBoardGame(id);

        // then
        verify(boardGameRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to remove non existing board game")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingBoardGame() {
        // given
        long id = 100l;
        String exMsg = "No such board game with id: " + id;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .count() > 0);

        // when
        assertThatThrownBy(() -> boardGameService.deleteBoardGame(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
    }
}