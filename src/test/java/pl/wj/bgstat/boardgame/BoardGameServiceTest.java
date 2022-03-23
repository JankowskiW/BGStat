package pl.wj.bgstat.boardgame;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.ceil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardGameServiceTest {

    @Mock
    private BoardGameRepository boardGameRepository;
    @InjectMocks
    private BoardGameService boardGameService;

    private static final int PAGE_SIZE = 5;
    private static final int NUMBER_OF_ELEMENTS = 25;
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

    @BeforeEach
    void setUp() {
        populateBoardGameHeaderDtoList(NUMBER_OF_ELEMENTS);
        populateBoardGameList(NUMBER_OF_ELEMENTS);
    }

    @Test
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
                .doesNotContain(boardGameHeaderList.get(toIndex));
    }

    @Test
    void shouldReturnOnlyLastPageOfBoardGameHeaders() {
        // given
        int lastPageNumber = (int) ceil(boardGameHeaderList.size() / (double) PAGE_SIZE);
        int fromIndex = (boardGameHeaderList.size() / PAGE_SIZE) * PAGE_SIZE;
        int toIndex = boardGameHeaderList.size();
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class)))
                .willReturn(boardGameHeaderList.subList(fromIndex, toIndex));

        // when
        List<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(lastPageNumber, PAGE_SIZE);

        // then
        assertThat(boardGameHeaders)
                .isNotNull()
                .hasSizeLessThanOrEqualTo(PAGE_SIZE)
                .doesNotContain(boardGameHeaderList.get(fromIndex-1));
    }

    @Test
    void shouldReturnEmptyListOfBoardGameHeaders() {
        // given
        int tooHighPageNumber = (int) ceil(boardGameHeaderList.size() / (double) PAGE_SIZE);
        given(boardGameRepository.findAllBoardGameHeaders(any(Pageable.class)))
                .willReturn(new ArrayList<BoardGameHeaderDto>());

        // when
        List<BoardGameHeaderDto> boardGameHeaders =
                boardGameService.getBoardGameHeaders(tooHighPageNumber, PAGE_SIZE);

        // then
        assertThat(boardGameHeaders)
                .isNotNull()
                .hasSize(0)
                .doesNotContainAnyElementsOf(boardGameHeaderList);
    }

    @Test
    void shouldReturnSingleBoardGameDetailsById() {
        // given
        long id = 1l;
        given(boardGameRepository.findWithDescriptionById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .findAny()
                        .orElse(null));

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.getSingleBoardGame(id);

        // then
        assertThat(boardGameResponseDto).isNotNull();
        assertThat(boardGameResponseDto.getId()).isEqualTo(id);
        assertThat(boardGameResponseDto.getDescription()).isNotBlank();
    }

    @Test
    void shouldThrowEntityNotFoundException() {
        // given
        long id = boardGameList.size() + 1;
        String exMsg = "No such board game with id: " + id;
        given(boardGameRepository.findWithDescriptionById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .findAny()
                        .orElse(null));

        // when // then
        assertThatThrownBy(() -> boardGameService.getSingleBoardGame(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
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
            boardGame.setName(RandomString.make(Rand(MIN_NAME_LEN, MAX_NAME_LEN)));
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

//        boardGameHeaderDtos.add(new BoardGameHeaderDto(1, "A"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(2, "B"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(3, "C"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(4, "D"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(5, "E"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(6, "F"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(7, "G"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(8, "H"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(9, "I"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(10, "J"));
//        boardGameHeaderDtos.add(new BoardGameHeaderDto(11, "K"));

}