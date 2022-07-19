package pl.wj.bgstat.domain.userboardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.domain.boardgame.BoardGameRepository;
import pl.wj.bgstat.domain.store.StoreRepository;
import pl.wj.bgstat.domain.user.UserRepository;
import pl.wj.bgstat.domain.user.model.User;
import pl.wj.bgstat.domain.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.domain.userboardgame.model.UserBoardGameMapper;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameResponseDto;
import pl.wj.bgstat.exception.BadRequestException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;
    private final BoardGameRepository boardGameRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Page<UserBoardGameHeaderDto> getAuthorizedUserBoardGame(Pageable pageable, String username) {
        if (username == null || username.isEmpty()) throw new BadRequestException();
        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USER_RESOURCE_NAME, "username", username));
        return userBoardGameRepository.findUserBoardGameHeaders(user.getId(), pageable);
    }

    public UserBoardGameDetailsDto getSingleUserBoardGame(long id) {
        UserBoardGameDetailsDto userBoardGameDetailsDto = userBoardGameRepository.getWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
        return userBoardGameDetailsDto;
    }

    public UserBoardGameResponseDto addUserBoardGame(UserBoardGameRequestDto userBoardGameRequestDto) {
        throwExceptionWhenNotExistsById(userBoardGameRequestDto.getBoardGameId(), boardGameRepository);
        throwExceptionWhenNotExistsById(userBoardGameRequestDto.getUserId(), userRepository);
        if (userBoardGameRequestDto.getStoreId() != null) {
            throwExceptionWhenNotExistsById(userBoardGameRequestDto.getStoreId(), storeRepository);
        }
        UserBoardGame userBoardGame = UserBoardGameMapper.mapToUserBoardGame(userBoardGameRequestDto);
        userBoardGameRepository.save(userBoardGame);
        return UserBoardGameMapper.mapToUserBoardGameResponseDto(userBoardGame);
    }

    public UserBoardGameResponseDto editUserBoardGame(long id, UserBoardGameRequestDto userBoardGameRequestDto) {
        throwExceptionWhenNotExistsById(id, userBoardGameRepository);
        if (userBoardGameRequestDto.getStoreId() != null) {
            throwExceptionWhenNotExistsById(userBoardGameRequestDto.getStoreId(), storeRepository);
        }
        throwExceptionWhenForeignKeyConstraintViolationOccur(userBoardGameRequestDto.getUserId(), userRepository);
        UserBoardGame userBoardGame = UserBoardGameMapper.mapToUserBoardGame(id, userBoardGameRequestDto);
        userBoardGameRepository.save(userBoardGame);
        return UserBoardGameMapper.mapToUserBoardGameResponseDto(userBoardGame);
    }

    public void deleteUserBoardGame(long id) {
        throwExceptionWhenNotExistsById(id, userBoardGameRepository);
        userBoardGameRepository.deleteById(id);
    }
}
