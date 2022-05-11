package pl.wj.bgstat.userboardgame;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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

    /**
     * 1) Get User Board Game Header Lists
     *      - Get only one but not last page of User Board Game Headers by User id
     *      - Get only last page of User Board Game Headers by User id
     *      - Get empty list of User Board Game Headers by User id
     * 2) Get single User Board Game
     *      - Get single User Board Game
     *      - Try to get User Board Game by not existing User Board Game id
     * 3) Add new User Board Game
     *      - Add correctly
     *      - Try to add not existing board game as user board game
     *      - Try to add board game to not existing user
     *      - Try to add board game with not existing shop
     * 4) Edit User Board Game
     *      - Edit correctly
     *      - Try to edit not existing user board game (by id)
     *      - Try to set not existing shop to board game
     * 5) Delete User Board Game
     *      - Delete correctly
     *      - Try to delete not existing user board game (by id)
     */

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
    @DisplayName("Should throw ResourceNotFoundException when Shop not exists")
    void shouldThrowExceptionWhenShopNotExists() {
        // given
        long shopId = 100L;
        UserBoardGameRequestDto userBoardGameRequestDto = new UserBoardGameRequestDto();
        userBoardGameRequestDto.setShopId(shopId);
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userBoardGameService.addUserBoardGame(userBoardGameRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_RESOURCE_NAME, ID_FIELD, shopId));
    }

}