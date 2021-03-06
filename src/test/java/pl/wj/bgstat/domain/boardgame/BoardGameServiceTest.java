package pl.wj.bgstat.domain.boardgame;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.domain.attribute.AttributeRepository;
import pl.wj.bgstat.domain.boardgame.model.BoardGame;
import pl.wj.bgstat.domain.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.domain.boardgame.model.dto.*;
import pl.wj.bgstat.exception.*;
import pl.wj.bgstat.domain.gameplay.GameplayRepository;
import pl.wj.bgstat.domain.rulebook.RulebookService;
import pl.wj.bgstat.domain.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.domain.userboardgame.UserBoardGameRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class BoardGameServiceTest {

    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @Mock
    private GameplayRepository gameplayRepository;
    @Mock
    private RulebookService rulebookService;
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
    private static final String THUMBNAILS_PATH = "\\\\localhost\\resources\\thumbnails";

    private static MockedStatic ms;
    private static MultipartFile okFile;
    private static MultipartFile notOkFileMT;
    private static MultipartFile notOkFileRes;
    private static BufferedImage okBi;
    private static BufferedImage notOkBiRes;
    private static String okMediaType = "image/png";
    private static String notOkMediaType = "image/txt";

    private List<BoardGame> boardGameList;
    private List<BoardGameHeaderDto> boardGameHeaderList;

    @BeforeEach
    void setUp() {
        boardGameList = BoardGameServiceTestHelper.populateBoardGameList(NUMBER_OF_ELEMENTS);
        boardGameHeaderList = BoardGameServiceTestHelper.populateBoardGameHeaderDtoList(boardGameList);
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        okFile = BoardGameServiceTestHelper.createMultipartFile(okMediaType, true);
        notOkFileMT = BoardGameServiceTestHelper.createMultipartFile(notOkMediaType, true);
        notOkFileRes = BoardGameServiceTestHelper.createMultipartFile(okMediaType, false);
        okBi = ImageIO.read(okFile.getInputStream());
        notOkBiRes = ImageIO.read(notOkFileRes.getInputStream());
        ms = mockStatic(ImageIO.class);
    }

    @AfterAll
    static void afterAll() {
        ms.close();
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
    void shouldReturnCreatedBoardGame() throws IOException {
        // given
        BoardGameRequestDto boardGameRequestDto = BoardGameServiceTestHelper.createBoardGameRequestDto(
                NUMBER_OF_ELEMENTS, BOARD_GAME_DEFAULT_OBJECT_TYPE_ID);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGame.setId(boardGameList.size()+1);
        boardGame.getBoardGameDescription().setBoardGameId(boardGame.getId());
        BoardGameResponseDto expectedResponse = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        given(boardGameRepository.existsByName(anyString())).willReturn(
                boardGameList.stream().anyMatch(bg -> bg.getName().equals(boardGameRequestDto.getName())));
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(ImageIO.read(any(InputStream.class))).willReturn(okBi);
        given(boardGameRepository.save(any(BoardGame.class))).willAnswer(
                i -> {
                    BoardGame bg = i.getArgument(0, BoardGame.class);
                    bg.setId(boardGame.getId());
                    bg.getBoardGameDescription().setBoardGameId(boardGame.getId());
                    return bg;
                });

        // when
        BoardGameResponseDto boardGameResponseDto = boardGameService.addBoardGame(boardGameRequestDto, okFile);

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
        BoardGameResponseDto boardGameResponseDto = boardGameService.addBoardGame(boardGameRequestDto, null);

        // then
        assertThat(boardGameResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
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
                .hasMessage(createResourceExistsExceptionMessage(BOARD_GAME_RESOURCE_NAME, Optional.of(NAME_FIELD)));
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
        BoardGameResponseDto boardGameResponseDto = boardGameService.editBoardGame(id, boardGameRequestDto, okFile);

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
        BoardGameResponseDto boardGameResponseDto = boardGameService.editBoardGame(id, boardGameRequestDto, okFile);

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
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, boardGameRequestDto, okFile))
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
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, new BoardGameRequestDto(), okFile))
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
        assertThatThrownBy(() -> boardGameService.editBoardGame(id, boardGameRequestDto, okFile))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(BOARD_GAME_RESOURCE_NAME, Optional.of(NAME_FIELD)));
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
    @DisplayName("Should add new thumbnail when board game exists")
    void shouldAddNewThumbnailWhenBoardGameExists() throws IOException {
        // given
        long boardGameId = 1L;
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.findThumbnailPath(anyLong())).willReturn(new BoardGameThumbnailResponseDto(boardGameId, null));
        given(ImageIO.read(any(InputStream.class))).willReturn(okBi);

        // when
        BoardGameThumbnailResponseDto boardGameThumbnailResponseDto = boardGameService.addOrReplaceThumbnail(boardGameId, okFile);

        // then
        assertThat(boardGameThumbnailResponseDto).isNotNull();
        assertThat(boardGameThumbnailResponseDto.getId()).isEqualTo(boardGameId);
        assertThat(boardGameThumbnailResponseDto.getThumbnailPath()).startsWith(THUMBNAILS_PATH);
    }

    @Test
    @DisplayName("Should replace thumbnail when board game exists")
    void shouldReplaceThumbnailWhenBoardGameExists() throws IOException {
        // given
        long boardGameId = 1L;
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.findThumbnailPath(anyLong())).willReturn(new BoardGameThumbnailResponseDto(boardGameId, okFile.getName()));
        given(ImageIO.read(any(InputStream.class))).willReturn(okBi);

        // when
        BoardGameThumbnailResponseDto boardGameThumbnailResponseDto = boardGameService.addOrReplaceThumbnail(boardGameId, okFile);

        // then
        assertThat(boardGameThumbnailResponseDto).isNotNull();
        assertThat(boardGameThumbnailResponseDto.getId()).isEqualTo(boardGameId);
        assertThat(boardGameThumbnailResponseDto.getThumbnailPath()).startsWith(THUMBNAILS_PATH);
    }

    @Test
    @DisplayName("Should throw UnsupportedFileMediaTypeException when given media type is unsupported")
    void shouldThrowExceptionWhenGivenMediaTypeIsUnsupported() {
        // given
        long id = 1L;
        BoardGameThumbnailResponseDto boardGameThumbnailResponseDto = new BoardGameThumbnailResponseDto(id, null);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.findThumbnailPath(anyLong())).willReturn(boardGameThumbnailResponseDto);

        // when
        assertThatThrownBy(() -> boardGameService.addOrReplaceThumbnail(id, notOkFileMT))
                .isInstanceOf(UnsupportedFileMediaTypeException.class)
                .hasMessage(String.format("Unsupported %s media type. Supported media types: %s", notOkMediaType, ExceptionHelper.SUPPORTED_THUMBNAIL_MEDIA_TYPES));
    }

    @Test
    @DisplayName("Should throw RequestFileException when image size or resolution is not correct")
    void shouldThrowExceptionWhenImageSizeOrResolutionIsNotCorrect() throws IOException {
        // given
        long id = 1L;
        BoardGameThumbnailResponseDto boardGameThumbnailResponseDto = new BoardGameThumbnailResponseDto(id, null);

        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.findThumbnailPath(anyLong())).willReturn(boardGameThumbnailResponseDto);
        given(ImageIO.read(any(InputStream.class))).willReturn(notOkBiRes);

        // when
        assertThatThrownBy(() -> boardGameService.addOrReplaceThumbnail(id, notOkFileRes))
                .isInstanceOf(RequestFileException.class)
                .hasMessage(createRequestFileExceptionMessage(
                        "Thumbnail", MIN_THUMBNAIL_HEIGHT, MAX_THUMBNAIL_HEIGHT,
                        MIN_THUMBNAIL_WIDTH, MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_SIZE));
    }

    @Test
    @DisplayName("Should throw RequestFileException when IOException was thrown")
    void shouldThrowExceptionWhenIOExceptionWasThrown() throws IOException {
        // given
        long id = 1L;
        String mediaType = "image/png";
        BoardGameThumbnailResponseDto boardGameThumbnailResponseDto = new BoardGameThumbnailResponseDto(id, null);
        MultipartFile file = BoardGameServiceTestHelper.createMultipartFile(mediaType, true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.findThumbnailPath(anyLong())).willReturn(boardGameThumbnailResponseDto);
        given(ImageIO.read(any(InputStream.class))).willThrow(IOException.class);
        // when
        assertThatThrownBy(() -> boardGameService.addOrReplaceThumbnail(id, file))
                .isInstanceOf(RequestFileException.class)
                .hasMessage(createRequestFileExceptionSaveFailedMessage(file.getName()));
    }

    @Test
    @DisplayName("Should remove board game and assigned attributes by id when id exists in database")
    void shouldRemoveBoardGameByIdWhenIdExists () throws IOException {
        // given
        long id = 3L;
        BoardGame boardGame = new BoardGame();
        boardGame.setId(id);
        boardGame.setObjectTypeId(1L);
        boardGame.setThumbnailPath(String.format("%s\\%s.jpeg",THUMBNAILS_PATH, UUID.randomUUID()));
        given(boardGameRepository.findById(anyLong())).willReturn(Optional.of(boardGame));
        willDoNothing().given(attributeRepository).deleteByObjectIdAndObjectTypeId(anyLong(), anyLong());
        willDoNothing().given(userBoardGameRepository).deleteByBoardGameId(anyLong());
        willDoNothing().given(rulebookService).deleteAllRulebooksByBoardGameId(anyLong());
        willDoNothing().given(gameplayRepository).deleteByBoardGameId(anyLong());
        willDoNothing().given(boardGameRepository).deleteById(anyLong());
        given(boardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        boardGameService.deleteBoardGame(id);

        // then
        verify(attributeRepository).deleteByObjectIdAndObjectTypeId(id, boardGame.getObjectTypeId());
        verify(userBoardGameRepository).deleteByBoardGameId(id);
        verify(rulebookService).deleteAllRulebooksByBoardGameId(id);
        verify(gameplayRepository).deleteByBoardGameId(id);
        verify(boardGameRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw FileException when cannot remove rulebooks file or directory")
    void shouldThrowExceptionWhenCannotRemoveRulebooksFileOrDirectory() throws IOException {
        // given
        long id = 3L;
        BoardGame boardGame = new BoardGame();
        boardGame.setId(id);
        boardGame.setObjectTypeId(1L);
        boardGame.setThumbnailPath(String.format("%s\\%s.jpeg",THUMBNAILS_PATH, UUID.randomUUID()));
        given(boardGameRepository.findById(anyLong())).willReturn(Optional.of(boardGame));
        willDoNothing().given(attributeRepository).deleteByObjectIdAndObjectTypeId(anyLong(), anyLong());
        willDoNothing().given(userBoardGameRepository).deleteByBoardGameId(anyLong());
        willThrow(IOException.class).given(rulebookService).deleteAllRulebooksByBoardGameId(anyLong());

        // when
        assertThatThrownBy(() -> boardGameService.deleteBoardGame(id))
                .isInstanceOf(FileException.class)
                .hasMessage("Cannot remove rulebooks file or directory");

        // then
        verify(attributeRepository).deleteByObjectIdAndObjectTypeId(id, boardGame.getObjectTypeId());
        verify(userBoardGameRepository).deleteByBoardGameId(id);
        verify(rulebookService).deleteAllRulebooksByBoardGameId(id);
        verify(gameplayRepository, never()).deleteByBoardGameId(id);
        verify(boardGameRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing board game")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingBoardGame() {
        // given
        long id = 100L;
        given(boardGameRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> boardGameService.deleteBoardGame(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }
}