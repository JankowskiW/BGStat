package pl.wj.bgstat.boardgame;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardGameServiceTest {

    @Mock
    private BoardGameRepository boardGameRepository;
    @InjectMocks
    private BoardGameService boardGameService;

    private static final int PAGE_SIZE = 4;
    private static final int NUMBER_OF_ELEMENTS = 20;

    private static final int MIN_NAME_LEN = 1;
    private static final int MAX_NAME_LEN = 255;
    private static final int MIN_AGE = 1;
    private static final int MAX_AGE = 18;
    private static final int MIN_PLAYERS_NUMBER = 1;
    private static final int MAX_PLAYERS_NUMBER = 1;
    private static final int MIN_COMPLEXITY = 1;
    private static final int MAX_COMPLEXITY = 10;
    private static final int MIN_PLAYING_TIME = 1;
    private static final int MAX_PLAYING_TIME = 360;

    private List<BoardGameHeaderDto> boardGameHeaderList;
    private List<BoardGame> boardGameList;
    private BoardGameRequestDto boardGameRequestDto;

    @BeforeEach
    void setUp() {
        populateBoardGameHeaderDtoList(NUMBER_OF_ELEMENTS);
        populateBoardGameList(NUMBER_OF_ELEMENTS);
        createBoardGameRequestDto();
    }


    @Test
    @DisplayName("Should return only one but not last page of board game headers")
    void shouldReturnOnlyOneButNotLastPageOfBoardGameHeaders() {
        // given
        int pageNumber = 2;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class)))
                .willReturn(boardGameHeaderList.subList(fromIndex, toIndex));

        // when
        List<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(pageNumber, PAGE_SIZE);

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
                .willReturn(boardGameHeaderList.subList(fromIndex, toIndex));

        // when
        List<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(lastPageNumber, PAGE_SIZE);

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
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class))).willReturn(new ArrayList<>());

        // when
        List<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(tooHighPageNumber, PAGE_SIZE);

        // then
        verify(boardGameRepository).findAllBoardGameHeaders(PageRequest.of(tooHighPageNumber, PAGE_SIZE));
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
        assertThatThrownBy(() -> boardGameService.getBoardGameHeaders(pageNumber, PAGE_SIZE))
                .isInstanceOf(IllegalAccessError.class)
                .hasMessage(exMsg);

        // then
        verify(boardGameRepository).findAllBoardGameHeaders(PageRequest.of(pageNumber, PAGE_SIZE));
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
        verify(boardGameRepository).findWithDescriptionById(id);
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

        // then
        verify(boardGameRepository).findWithDescriptionById(id);
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
        verify(boardGameRepository).existsByName(boardGameRequestDto.getName());
        verify(boardGameRepository).save(any(BoardGame.class));
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

        // then
        verify(boardGameRepository).existsByName(boardGameName);
    }

    @Test
    @DisplayName("Should remove board game by id when id exists in database")
    void shouldRemoveBoardGameByIdWhenIdExists () {
        // given
        long id = 3;
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
        long id = 100;
        String exMsg = "No such board game with id: " + id;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .count() > 0);

        // when
        assertThatThrownBy(() -> boardGameService.deleteBoardGame(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);

        // then
        verify(boardGameRepository).existsById(any());
    }







    private int Rand(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    private void populateBoardGameHeaderDtoList(int numberOfElements) {
        boardGameHeaderList = new ArrayList<>();
        for (int i = 1; i <= numberOfElements; i++)
            boardGameHeaderList.add(
                    new BoardGameHeaderDto(i, RandomString.make(Rand(MIN_NAME_LEN, MAX_NAME_LEN))));
    }

    private void populateBoardGameList(int numberOfElements) {
        BoardGame boardGame;
        BoardGameDescription boardGameDescription;
        boardGameList = new ArrayList<>();
        for (int i = 1; i <= numberOfElements; i++) {
            boardGame = new BoardGame();
            boardGame.setId(i);
            boardGame.setName("Name No. " + i);
            boardGame.setRecommendedAge(Rand(MIN_AGE, MAX_AGE));
            boardGame.setMinPlayersNumber(Rand(MIN_PLAYERS_NUMBER, MAX_PLAYERS_NUMBER));
            boardGame.setMaxPlayersNumber(Rand(boardGame.getMinPlayersNumber(), MAX_PLAYERS_NUMBER));
            boardGame.setComplexity(Rand(MIN_COMPLEXITY, MAX_COMPLEXITY));
            boardGame.setPlayingTime(Rand(MIN_PLAYING_TIME, MAX_PLAYING_TIME));
            boardGameDescription = new BoardGameDescription();
            boardGameDescription.setBoardGameId(i);
            boardGameDescription.setBoardGame(boardGame);
            boardGameDescription.setDescription("DESCRIPTION OF " + boardGame.getName());
            boardGame.setBoardGameDescription(boardGameDescription);
            boardGameList.add(boardGame);
        }
    }

    private void createBoardGameRequestDto() {
        boardGameRequestDto = new BoardGameRequestDto(
                "Name No. " + (boardGameList.size() + 1),
                18,
                1,
                4,
                5,
                150,
                "DESCRIPTION OF Name No. " + (boardGameList.size() + 1));
    }

}