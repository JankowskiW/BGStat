package pl.wj.bgstat.userboardgame;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.shop.ShopRepository;
import pl.wj.bgstat.user.UserRepository;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.UserBoardGameMapper;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;
import static pl.wj.bgstat.userboardgame.UserBoardGameServiceTestHelper.populateUserBoardGameHeaderDtoList;
import static pl.wj.bgstat.userboardgame.UserBoardGameServiceTestHelper.populateUserBoardGameResponseDtoList;

@ExtendWith(MockitoExtension.class)
class  UserBoardGameServiceTest {

    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    private BoardGameRepository boardGameRepository;
    private UserRepository userRepository;
    private ShopRepository shopRepository;
    @InjectMocks
    private UserBoardGameService userBoardGameService;

    private static final int NUMBER_OF_ELEMENTS = 20;
    private static final int PAGE_SIZE = 3;

    private List<UserBoardGameDetailsDto> userBoardGameDetailsDtoList;
    private List<UserBoardGameHeaderDto> userBoardGameHeaderDtoList;

    @BeforeEach
    void setUp() {
        userBoardGameDetailsDtoList = populateUserBoardGameResponseDtoList(NUMBER_OF_ELEMENTS);
        userBoardGameHeaderDtoList = populateUserBoardGameHeaderDtoList(userBoardGameDetailsDtoList);
    }

    @Test
    @DisplayName("Should return single user board game")
    void shouldReturnSingleUserBoardGameDetailsById() {
        // given
        long id = 1L;
        Optional<UserBoardGameDetailsDto> returnedUserBoardGameDto =
                userBoardGameDetailsDtoList.stream().filter(ubg -> ubg.getId() == id).findAny();
        given(userBoardGameRepository.findWithDetailsById(anyLong())).willReturn(returnedUserBoardGameDto);

        // when
        UserBoardGameDetailsDto userBoardGameDetailsDto = userBoardGameService.getSingleUserBoardGame(id);

        // then
        assertThat(userBoardGameDetailsDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(returnedUserBoardGameDto);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cannot find user board game by id")
    void shouldThrowExceptionWhenCannotFindUserBoardGameById() {
        // given
        long id = 100L;
        given(userBoardGameRepository.findWithDetailsById(anyLong()))
                .willReturn(userBoardGameDetailsDtoList.stream()
                        .filter(ubg -> ubg.getId() == id)
                                .findAny());

        // when
        assertThatThrownBy(() -> userBoardGameService.getSingleUserBoardGame(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should create and return created user board game")
    void shouldReturnCreatedUserBoardGame() {
        // given
        UserBoardGameRequestDto userBoardGameRequestDto = UserBoardGameServiceTestHelper.createUserBoardGameRequestDto();
        UserBoardGame userBoardGame = UserBoardGameMapper.mapToUserBoardGame(userBoardGameRequestDto);
        userBoardGame.setId(userBoardGameHeaderDtoList.size()+1);
        UserBoardGameResponseDto expectedResponse = UserBoardGameMapper.mapToUserBoardGameResponseDto(userBoardGame);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(shopRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.save(any(UserBoardGame.class))).willAnswer(
                i -> {
                    UserBoardGame ubg = i.getArgument(0, UserBoardGame.class);
                    ubg.setId(userBoardGame.getId());
                    return ubg;
                });

        // when
        UserBoardGameResponseDto userBoardGameResponseDto =
                userBoardGameService.addUserBoardGame(userBoardGameRequestDto);

        // then
        assertThat(userBoardGameResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when BoardGame not exists")
    void shouldThrowExceptionWhenBoardGameNotExists() {
        // given
        long boardGameId = 100L;
        UserBoardGameRequestDto userBoardGameRequestDto = new UserBoardGameRequestDto();
        userBoardGameRequestDto.setBoardGameId(boardGameId);
        given(boardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.addUserBoardGame(userBoardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(BOARD_GAME_RESOURCE_NAME, ID_FIELD, boardGameId));

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when User not exists")
    void shouldThrowExceptionWhenUserNotExists() {
        // given
        long userId = 100L;
        UserBoardGameRequestDto userBoardGameRequestDto = new UserBoardGameRequestDto();
        userBoardGameRequestDto.setUserId(userId);
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.addUserBoardGame(userBoardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_RESOURCE_NAME, ID_FIELD, userId));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when Shop of new UserBoardGame not exists")
    void shouldThrowExceptionWhenShopOfNewUserBoardGameNotExists() {
        // given
        long shopId = 100L;
        UserBoardGameRequestDto userBoardGameRequestDto = new UserBoardGameRequestDto();
        userBoardGameRequestDto.setShopId(shopId);
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.addUserBoardGame(userBoardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SHOP_RESOURCE_NAME, ID_FIELD, shopId));
    }

    @Test
    @DisplayName("Should edit user board game when exists")
    void shouldEditUserBoardGameWhenExists() {
        // given
        long id = 1L;
        UserBoardGame userBoardGame = UserBoardGameServiceTestHelper.createUserBoardGame(id, 1,1,1);
        UserBoardGameRequestDto userBoardGameRequestDto =
                UserBoardGameRequestDto.builder().shopId(userBoardGame.getShopId()+1).build();
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        given(shopRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.save(any(UserBoardGame.class))).willAnswer(
                i -> {
                    UserBoardGame ubg = i.getArgument(0, UserBoardGame.class);
                    ubg.setId(id);
                    return ubg;
                });

        // when
        UserBoardGameResponseDto userBoardGameResponseDto = userBoardGameService.editUserBoardGame(id, userBoardGameRequestDto);

        // then
        assertThat(userBoardGameResponseDto).isNotNull();
        assertThat(userBoardGameResponseDto.getId()).isEqualTo(id);
        assertThat(userBoardGameResponseDto.getShopId()).isEqualTo(userBoardGameRequestDto.getShopId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when UserBoardGame not exists")
    void shouldThrowExceptionWhenUserBoardGameNotExists() {
        // given
        long id = 100L;
        UserBoardGameRequestDto userBoardGameRequestDto = new UserBoardGameRequestDto();
        given(userBoardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.editUserBoardGame(id, userBoardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when Shop of edited UserBoardGame not exists")
    void shouldThrowExceptionWhenShopOfEditedUserBoardGameNotExists() {
        // given
        long id = 1L;
        long shopId = 100L;
        UserBoardGameRequestDto userBoardGameRequestDto = new UserBoardGameRequestDto();
        given(shopRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.editUserBoardGame(id, userBoardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SHOP_RESOURCE_NAME, ID_FIELD, shopId));
    }

    @Test
    @DisplayName("Should remove UserBoardGame by id when exists")
    void shouldRemoveUserBoardGameByIdWhenExists() {
        // given
        long id = 1L;
        given(userBoardGameRepository.existsById(anyLong())).willReturn(true);
        willDoNothing().given(userBoardGameRepository).deleteById(anyLong());

        // when
        userBoardGameService.deleteUserBoardGame(id);

        // then
        verify(userBoardGameRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing UserBoardGame")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingUserBoardGame() {
        // given
        long id = 100L;
        given(userBoardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.deleteUserBoardGame(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
    }
}