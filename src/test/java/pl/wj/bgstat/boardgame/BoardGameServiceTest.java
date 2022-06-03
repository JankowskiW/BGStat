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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGamePartialRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.exception.*;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.boardgame.BoardGameServiceTestHelper.*;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class BoardGameServiceTest {

    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @InjectMocks
    private BoardGameService boardGameService;

    private static final int PAGE_SIZE = 4;
    private static final int NUMBER_OF_ELEMENTS = 20;
    private static final long BOARD_GAME_DEFAULT_OBJECT_TYPE_ID = 1L;
    private static final int MIN_THUMBNAIL_HEIGHT = 600;
    private static final int MAX_THUMBNAIL_HEIGHT = 1200;
    private static final int MIN_THUMBNAIL_WIDTH = 600;
    private static final int MAX_THUMBNAIL_WIDTH = 1200;
    private static final int MAX_THUMBNAIL_SIZE = 10240;

    private List<BoardGame> boardGameList;
    private List<BoardGameHeaderDto> boardGameHeaderList;

    @BeforeEach
    void setUp() {
        boardGameList = populateBoardGameList(NUMBER_OF_ELEMENTS);
        boardGameHeaderList = populateBoardGameHeaderDtoList(boardGameList);
    }

    @Test
    @DisplayName("Should return only one but not last page of board game headers")
    void shouldReturnOnlyOneButNotLastPageOfBoardGameHeaders() {
        // given
        int pageNumber = 1;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        given(boardGameRepository.findBoardGameHeaders(any(Pageable.class)))
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
        given(boardGameRepository.findBoardGameHeaders(any(Pageable.class)))
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
    @DisplayName("Should return empty list of board game headers when page number is too high")
    void shouldReturnEmptyListOfBoardGameHeaders() {
        // given
        int tooHighPageNumber = (int) ceil(boardGameHeaderList.size() / (double) PAGE_SIZE) + 1;
        given(boardGameRepository.findBoardGameHeaders(any(Pageable.class)))
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
    @DisplayName("Should return only one board game details with description")
    void shouldReturnSingleBoardGameDetailsById() {
        // given
        long id = 1L;
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
    @DisplayName("Should throw ResourceNotFoundException when cannot find board game by id")
    void shouldThrowExceptionWhenCannotFindBoardGameById() {
        // given
        long id = boardGameList.size() + 1;
        given(boardGameRepository.findWithDescriptionById(anyLong()))
                .willReturn(boardGameList.stream()
                        .filter(bg -> bg.getId() == id)
                        .findAny());

        // when
        assertThatThrownBy(() -> boardGameService.getSingleBoardGame(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should create and return created board game")
    void shouldReturnCreatedBoardGame() {
        // given
        MultipartFile file = createMultipartFile("image/png", true);
        BoardGameRequestDto boardGameRequestDto = BoardGameServiceTestHelper.createBoardGameRequestDto(
                NUMBER_OF_ELEMENTS, BOARD_GAME_DEFAULT_OBJECT_TYPE_ID);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGame.setId(boardGameList.size()+1);
        boardGame.getBoardGameDescription().setBoardGameId(boardGame.getId());
        BoardGameResponseDto expectedResponse = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        given(boardGameRepository.existsByName(anyString())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getName().equals(boardGameRequestDto.getName())));
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.save(any(BoardGame.class))).willAnswer(
                i -> {
                    BoardGame bg = i.getArgument(0, BoardGame.class);
                    bg.setId(boardGame.getId());
                    bg.getBoardGameDescription().setBoardGameId(boardGame.getId());
                    return bg;
                });

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.addBoardGame(boardGameRequestDto, file);

        // then
        assertThat(boardGameResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return created board game with default system object type id")
    void shouldReturnCreatedBoardGameWithDefaultSystemObjectTypeIdWhenObjectTypeNotSet()  {
        // given
        MultipartFile file = createMultipartFile("image/png", true);
        BoardGameRequestDto boardGameRequestDto = BoardGameServiceTestHelper.createBoardGameRequestDto(
                NUMBER_OF_ELEMENTS, 0);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGame.setId(boardGameList.size()+1);
        boardGame.getBoardGameDescription().setBoardGameId(boardGame.getId());
        BoardGameResponseDto expectedResponse = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        expectedResponse.setObjectTypeId(BOARD_GAME_DEFAULT_OBJECT_TYPE_ID);
        given(boardGameRepository.existsByName(anyString())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getName().equals(boardGameRequestDto.getName())));
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.save(any(BoardGame.class))).willAnswer(
                i -> {
                    BoardGame bg = i.getArgument(0, BoardGame.class);
                    bg.setId(boardGame.getId());
                    bg.getBoardGameDescription().setBoardGameId(boardGame.getId());
                    return bg;
                });

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.addBoardGame(boardGameRequestDto, file);

        // then
        assertThat(boardGameResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }
    
    @Test
    @DisplayName("Should throw UnsupportedFileMediaTypeException when given media type is unsupported")
    void shouldThrowExceptionWhenGivenMediaTypeIsUnsupported() {
        // given
        String mediaType = "image/txt";
        String supportedMT = "[image/jpeg, image/png]";
        MultipartFile file = createMultipartFile(mediaType, true);
        BoardGameRequestDto boardGameRequestDto = createBoardGameRequestDto(1,1);
        given(boardGameRepository.existsByName(anyString())).willReturn(false);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> boardGameService.addBoardGame(boardGameRequestDto, file))
                .isInstanceOf(UnsupportedFileMediaTypeException.class)
                .hasMessage(String.format("Unsupported %s media type. Supported media types: %s", mediaType, ExceptionHelper.SUPPORTED_THUMBNAIL_MEDIA_TYPES.toString()));
    }

    @Test
    @DisplayName("Should throw RequestFileException when image size or resolution is not correct")
    void shouldThrowExceptionWhenImageSizeOrResolutionIsNotCorrect() {
        // given
        String mediaType = "image/png";
        MultipartFile file = createMultipartFile(mediaType, false);
        BoardGameRequestDto boardGameRequestDto = createBoardGameRequestDto(1,1);
        given(boardGameRepository.existsByName(anyString())).willReturn(false);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> boardGameService.addBoardGame(boardGameRequestDto, file))
                .isInstanceOf(RequestFileException.class)
                .hasMessage(createRequestFileExceptionMessage(
                        "Thumbnail", MIN_THUMBNAIL_HEIGHT, MAX_THUMBNAIL_HEIGHT,
                        MIN_THUMBNAIL_WIDTH, MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_SIZE));
    }

    @Test
    @DisplayName("Should throw RequestFileException when IOException was thrown")
    void shouldThrowExceptionWhenIOExceptionWasThrown() throws IOException {
        // given
        String mediaType = "image/png";
        MultipartFile file = createMultipartFile(mediaType, true);
        mockStatic(ImageIO.class);
        BoardGameRequestDto boardGameRequestDto = createBoardGameRequestDto(1,1);
        given(boardGameRepository.existsByName(anyString())).willReturn(false);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(ImageIO.read(any(InputStream.class))).willThrow(IOException.class);
        // when
        assertThatThrownBy(() -> boardGameService.addBoardGame(boardGameRequestDto, file))
                .isInstanceOf(RequestFileException.class)
                .hasMessage(createRequestFileExceptionSaveFailedMessage(file.getName()));
    }
    
    @Test
    @DisplayName("Should throw ResourceNotFoundException when SystemObjectType id does not exist in database")
    void shouldThrowExceptionWhenSystemObjectTypeIdDoesNotExist() {
        // given
        long systemObjectTypeId = 99L;
        String fileName = "sourceFile.png";
        String fileContent = "Example file content";
        MultipartFile file = new MockMultipartFile(fileName, fileContent.getBytes());
        BoardGameRequestDto boardGameRequestDto = new BoardGameRequestDto();
        boardGameRequestDto.setObjectTypeId(systemObjectTypeId);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> boardGameService.addBoardGame(boardGameRequestDto, file))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, systemObjectTypeId));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when board game name already exists in database")
    void shouldThrowExceptionWhenBoardGameNameExists() {
        // given
        String boardGameName = "Name No. 1";
        String fileName = "sourceFile.png";
        String fileContent = "Example file content";
        MultipartFile file = new MockMultipartFile(fileName, fileContent.getBytes());
        BoardGameRequestDto boardGameRequestDto = new BoardGameRequestDto(
                boardGameName,BOARD_GAME_DEFAULT_OBJECT_TYPE_ID, 1, 1, 5, 2, 150, "DESCRIPTION");
        given(boardGameRepository.existsByName(anyString()))
                .willReturn(boardGameList.stream().anyMatch(bg -> bg.getName().equals(boardGameName)));

        // when
        assertThatThrownBy(() -> boardGameService.addBoardGame(boardGameRequestDto, file))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(BOARD_GAME_RESOURCE_NAME, NAME_FIELD));
    }

    @Test
    @DisplayName("Should edit board game when exists")
    void shouldEditBoardGameWhenExists() {
        // given
        long id = 1L;
        BoardGame boardGame = boardGameList.stream().filter(bg -> bg.getId() == id).findFirst().orElseThrow();
        BoardGameRequestDto boardGameRequestDto = BoardGameRequestDto.builder()
                .name(boardGame.getName())
                .recommendedAge(boardGame.getRecommendedAge()*2)
                .objectTypeId(BOARD_GAME_DEFAULT_OBJECT_TYPE_ID)
                .build();
        given(boardGameRepository.existsById(anyLong())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getId() == id));
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getId() != id &&
                        bg.getName().equals(boardGameRequestDto.getName())));
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
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
    @DisplayName("Should set default system object type id when id is set to zero")
    void shouldSetDefaultSystemObjectTypeIdWhenIdIsSetToZero() {
        // given
        long id = 1L;
        BoardGame boardGame = boardGameList.stream().filter(bg -> bg.getId() == id).findFirst().orElseThrow();
        BoardGameRequestDto boardGameRequestDto = BoardGameRequestDto.builder()
                .name(boardGame.getName())
                .objectTypeId(0)
                .build();
        given(boardGameRepository.existsById(anyLong())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getId() == id));
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getId() != id &&
                        bg.getName().equals(boardGameRequestDto.getName())));
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
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
        assertThat(boardGameResponseDto.getObjectTypeId()).isEqualTo(BOARD_GAME_DEFAULT_OBJECT_TYPE_ID);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when SystemObjectType id of BoardGame does not exist in database")
    void shouldThrowExceptionWhenSystemObjectTypeIdOfBoardGameDoesNotExist() {
        // given
        long id = 1L;
        long objectTypeId = 100L;
        BoardGameRequestDto boardGameRequestDto = new BoardGameRequestDto();
        boardGameRequestDto.setObjectTypeId(objectTypeId);
        boardGameRequestDto.setName("TEST");
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(false);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, boardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, objectTypeId));

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit not existing board game")
    void shouldThrowExceptionWhenTryingToEditNotExistingBoardGame() {
        // given
        long id = 100L;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream().anyMatch(bg -> bg.getId() == id));

        // when
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, new BoardGameRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1L;
        BoardGame boardGame = boardGameList.stream().filter(bg -> bg.getId() == id).findFirst().orElseThrow();
        BoardGameRequestDto boardGameRequestDto = new BoardGameRequestDto(
                boardGameList.get(1).getName(), BOARD_GAME_DEFAULT_OBJECT_TYPE_ID, boardGame.getRecommendedAge(),
                boardGame.getMinPlayersNumber(), boardGame.getMaxPlayersNumber(), boardGame.getComplexity(),
                boardGame.getEstimatedPlaytime(), boardGame.getBoardGameDescription().getDescription());
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream().anyMatch(bg -> bg.getId() == id));
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong()))
                .willReturn(boardGameList.stream().anyMatch(bg -> bg.getId() != id && bg.getName().equals(boardGameRequestDto.getName())));

        // when
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, boardGameRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(BOARD_GAME_RESOURCE_NAME, NAME_FIELD));
    }

    @Test
    @DisplayName("Should partially edit board game when exists")
    void shouldPartiallyEditBoardGameWhenExists() {
        // given
        long id = 1L;
        BoardGame boardGame = boardGameList.stream().filter(bg -> bg.getId() == id).findFirst().orElseThrow();
        BoardGamePartialRequestDto boardGamePartialRequestDto = BoardGamePartialRequestDto.builder()
                .name("New board game name")
                .build();
        given(boardGameRepository.findWithDescriptionById(anyLong())).willReturn(Optional.of(boardGame));
        given(boardGameRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(false);
        given(boardGameRepository.save(any(BoardGame.class))).willAnswer(
                i -> {
                    BoardGame bg = i.getArgument(0, BoardGame.class);
                    bg.setId(id);
                    return bg;
                });

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.editBoardGamePartially(id, boardGamePartialRequestDto);

        // then
        assertThat(boardGameResponseDto).isNotNull();
        assertThat(boardGameResponseDto.getId()).isEqualTo(id);
        assertThat(boardGameResponseDto.getName()).isEqualTo(boardGamePartialRequestDto.getName());
    }

    @Test
    @DisplayName("Should remove board game and assigned attributes by id when id exists in database")
    void shouldRemoveBoardGameByIdWhenIdExists () {
        // given
        long id = 3L;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream().anyMatch(bg -> bg.getId() == id));
        willDoNothing().given(boardGameRepository).deleteById(anyLong());

        // when
        boardGameService.deleteBoardGame(id);

        // then
        verify(boardGameRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing board game")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingBoardGame() {
        // given
        long id = 100L;
        given(boardGameRepository.existsById(anyLong()))
                .willReturn(boardGameList.stream().anyMatch(bg -> bg.getId() == id));

        // when
        assertThatThrownBy(() -> boardGameService.deleteBoardGame(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }
}