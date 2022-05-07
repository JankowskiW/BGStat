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
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.userboardgame.UserBoardGameServiceTestHelper.populateUserBoardGameHeaderDtoList;
import static pl.wj.bgstat.userboardgame.UserBoardGameServiceTestHelper.populateUserBoardGameResponseDtoList;

@ExtendWith(MockitoExtension.class)
class  UserBoardGameServiceTest {

    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    @InjectMocks
    private UserBoardGameService userBoardGameService;

    private static final int NUMBER_OF_ELEMENTS = 20;
    private static final int PAGE_SIZE = 3;

    private List<UserBoardGameResponseDto> userBoardGameResponseDtoList;
    private List<UserBoardGameHeaderDto> userBoardGameHeaderDtoList;

    @BeforeEach
    void setUp() {
        userBoardGameResponseDtoList = populateUserBoardGameResponseDtoList(NUMBER_OF_ELEMENTS);
        userBoardGameHeaderDtoList = populateUserBoardGameHeaderDtoList(userBoardGameResponseDtoList);
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
    @DisplayName("Should return only one but not last page of user board game headers")
    void shouldReturnOnlyOneButNotLastPageOfUserBoardGameHeaders() {
        // given
        int pageNumber = 2;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        given(userBoardGameRepository.findUserBoardGameHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(userBoardGameHeaderDtoList.subList(fromIndex, toIndex)));

        // when
        Page<UserBoardGameHeaderDto> userBoardGameHeaders =
                userBoardGameService.getUserBoardGameHeaders(PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(userBoardGameHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(userBoardGameHeaderDtoList.subList(fromIndex, toIndex));
    }

    @Test
    @DisplayName("Should return only last page of user board game headers")
    void shouldReturnOnlyLastPageOfUserBoardGameHeaders() {
        // given
        int lastPageNumber = (int) ceil(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE);
        int lastPageSize = (NUMBER_OF_ELEMENTS - (int) floor(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE) * PAGE_SIZE);
        lastPageSize = lastPageSize == 0 ? PAGE_SIZE : lastPageSize;
        int fromIndex = NUMBER_OF_ELEMENTS - lastPageSize;
        int toIndex = NUMBER_OF_ELEMENTS;
        given(userBoardGameRepository.findUserBoardGameHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(userBoardGameHeaderDtoList.subList(fromIndex, toIndex)));

        // when
        Page<UserBoardGameHeaderDto> userBoardGameHeaders =
                userBoardGameService.getUserBoardGameHeaders(PageRequest.of(lastPageNumber, PAGE_SIZE));

        // then
        assertThat(userBoardGameHeaders)
                .isNotNull()
                .hasSize(lastPageSize)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(userBoardGameHeaderDtoList.subList(fromIndex,toIndex));
    }

    @Test
    @DisplayName("Should return empty list of user board game headers when page number is too high")
    void shouldReturnEmptyListOfUserBoardGameHeaders() {
        // given
        int tooHighPageNumber = (int) ceil(userBoardGameHeaderDtoList.size() / (double) PAGE_SIZE) + 1;
        given(userBoardGameRepository.findUserBoardGameHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));

        // when
        Page<UserBoardGameHeaderDto> userBoardGameHeaders =
                userBoardGameService.getUserBoardGameHeaders(PageRequest.of(tooHighPageNumber, PAGE_SIZE));

        // then
        assertThat(userBoardGameHeaders)
                .isNotNull()
                .hasSize(0);
    }

}