package pl.wj.bgstat.user;

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
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.gameplay.GameplayRepository;
import pl.wj.bgstat.gameplay.GameplayService;
import pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.exception.ExceptionHelper.*;
import static pl.wj.bgstat.gameplay.GameplayServiceTestHelper.createGameplaysStatsDto;
import static pl.wj.bgstat.user.UserServiceTestHelper.populateUserBoardGameHeaderList;
import static pl.wj.bgstat.user.UserServiceTestHelper.populateUserGameplayHeaderList;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    @Mock
    private GameplayRepository gameplayRepository;
    @Mock
    private GameplayService gameplayService;
    @InjectMocks
    private UserService userService;

    private static final int PAGE_SIZE = 3;
    private static final int NUMBER_OF_ELEMENTS = 20;

    private List<UserBoardGameHeaderDto> userBoardGameHeaderList;
    private List<GameplayHeaderDto> userGameplayHeaderList;

    @BeforeEach
    void setUp() {
        userBoardGameHeaderList = populateUserBoardGameHeaderList(NUMBER_OF_ELEMENTS);
        userGameplayHeaderList = populateUserGameplayHeaderList(NUMBER_OF_ELEMENTS);
    }

    @Test
    @DisplayName("Should return only one but not last page of user board game headers")
    void shouldReturnOnlyOneButNotLastPageOfUserBoardGameHeaders() {
        // given
        long userId = 1L;
        int pageNumber = 1;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        Page<UserBoardGameHeaderDto> expectedResponse = new PageImpl<>(userBoardGameHeaderList.subList(fromIndex,toIndex));
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.findUserBoardGameHeaders(anyLong(), any(Pageable.class)))
                .willReturn(expectedResponse);

        // when
        Page<UserBoardGameHeaderDto> userBoardGameHeaders =
                userService.getUserBoardGameHeaders(userId, PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(userBoardGameHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return only last page of user board game headers")
    void shouldReturnOnlyLastPageOfUserBoardGameHeaders() {
        // given
        long userId = 1L;
        int lastPageNumber = (int) ceil(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE);
        int lastPageSize = (NUMBER_OF_ELEMENTS - (int) floor(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE) * PAGE_SIZE);
        lastPageSize = lastPageSize == 0 ? PAGE_SIZE : lastPageSize;
        int fromIndex = NUMBER_OF_ELEMENTS - lastPageSize;
        int toIndex = NUMBER_OF_ELEMENTS;
        Page<UserBoardGameHeaderDto> expectedResponse = new PageImpl<>(userBoardGameHeaderList.subList(fromIndex,toIndex));
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.findUserBoardGameHeaders(anyLong(), any(Pageable.class)))
                .willReturn(expectedResponse);

        // when
        Page<UserBoardGameHeaderDto> userBoardGameHeaders =
                userService.getUserBoardGameHeaders(userId, PageRequest.of(lastPageNumber, PAGE_SIZE));

        // then
        assertThat(userBoardGameHeaders)
                .isNotNull()
                .hasSize(lastPageSize)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return empty list of user board game headers when page number is too high")
    void shouldReturnEmptyListOfBoardGameHeaders() {
        // given
        long userId = 1L;
        int tooHighPageNumber = (int) ceil(userBoardGameHeaderList.size() / (double) PAGE_SIZE) + 1;
        Page<UserBoardGameHeaderDto> userBoardGameHeaderPage = new PageImpl<>(new ArrayList<>());
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.findUserBoardGameHeaders(anyLong(), any(Pageable.class))).willReturn(userBoardGameHeaderPage);

        // when
        Page<UserBoardGameHeaderDto> userBoardGameHeaders =
                userService.getUserBoardGameHeaders(userId, PageRequest.of(tooHighPageNumber, PAGE_SIZE));

        // then
        assertThat(userBoardGameHeaders)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user id does not exist in database")
    void shouldThrowExceptionWhenUserIdDoesNotExists() {
        // given
        long userId = 100L;
        int pageNumber = 1;
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userService.getUserBoardGameHeaders(userId, PageRequest.of(pageNumber, PAGE_SIZE)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_RESOURCE_NAME, ID_FIELD, userId));
    }

    @Test
    @DisplayName("Should return only one but not last page of user gameplay headers")
    void shouldReturnOnlyOnePageOfUserGameplayHeaders() {
        // given
        long id = 1L;
        int pageNumber = 1;
        int fromIndex = 0;
        int toIndex = PAGE_SIZE;
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.findUserGameplayHeaders(anyLong(), any(Pageable.class)))
                .willReturn(new PageImpl<>(userGameplayHeaderList.subList(fromIndex, toIndex)));

        // when
        Page<GameplayHeaderDto> gameplayHeaders =
                userService.getUserGameplayHeaders(id, PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(gameplayHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(userGameplayHeaderList.subList(fromIndex,toIndex));
    }

    @Test
    @DisplayName("Should return only last page of user gameplay headers")
    void shouldReturnOnlyLastPageOfUserGameplayHeaders() {
        // given
        long id = 1L;
        int pageNumber = 5;
        int fromIndex = userGameplayHeaderList.size() - 3;
        int toIndex = fromIndex + PAGE_SIZE;
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.findUserGameplayHeaders(anyLong(), any(Pageable.class)))
                .willReturn(new PageImpl<>(userGameplayHeaderList.subList(fromIndex, toIndex)));

        // when
        Page<GameplayHeaderDto> gameplayHeaders =
                userService.getUserGameplayHeaders(id, PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(gameplayHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(userGameplayHeaderList.subList(fromIndex, toIndex));
    }

    @Test
    @DisplayName("Should return empty list of gameplay headers when page number is too high")
    void shouldReturnEmptyListOfGameplayHeaders() {
        // given
        long id = 1L;
        int tooHighPageNumber = 100;
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(gameplayRepository.findUserGameplayHeaders(anyLong(),any(Pageable.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));

        // when
        Page<GameplayHeaderDto> gameplayHeaders =
                userService.getUserGameplayHeaders(id, PageRequest.of(tooHighPageNumber, PAGE_SIZE));

        // then
        assertThat(gameplayHeaders)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // given
        long id = 100L;
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> userService.getUserBoardGameHeaders(id, PageRequest.of(1, PAGE_SIZE)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(USER_RESOURCE_NAME, ID_FIELD, id));
    }
}