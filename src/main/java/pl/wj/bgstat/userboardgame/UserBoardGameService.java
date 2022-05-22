package pl.wj.bgstat.userboardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.store.StoreRepository;
import pl.wj.bgstat.user.UserRepository;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.UserBoardGameMapper;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;
    private final BoardGameRepository boardGameRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public UserBoardGameDetailsDto getSingleUserBoardGame(long id) {
        UserBoardGameDetailsDto userBoardGameDetailsDto = userBoardGameRepository.getWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
        return userBoardGameDetailsDto;
    }

    public UserBoardGameResponseDto addUserBoardGame(UserBoardGameRequestDto userBoardGameRequestDto) {
        throwExceptionWhenNotExistsById(userBoardGameRequestDto.getBoardGameId(), boardGameRepository);
        throwExceptionWhenNotExistsById(userBoardGameRequestDto.getUserId(), userRepository);
        throwExceptionWhenNotExistsById(userBoardGameRequestDto.getStoreId(), storeRepository);
        UserBoardGame userBoardGame = UserBoardGameMapper.mapToUserBoardGame(userBoardGameRequestDto);
        userBoardGameRepository.save(userBoardGame);
        return UserBoardGameMapper.mapToUserBoardGameResponseDto(userBoardGame);
    }

    public UserBoardGameResponseDto editUserBoardGame(long id, UserBoardGameRequestDto userBoardGameRequestDto) {
        throwExceptionWhenNotExistsById(id, userBoardGameRepository);
        throwExceptionWhenNotExistsById(userBoardGameRequestDto.getStoreId(), storeRepository);
        UserBoardGame userBoardGame = UserBoardGameMapper.mapToUserBoardGame(userBoardGameRequestDto);
        userBoardGameRepository.save(userBoardGame);
        return UserBoardGameMapper.mapToUserBoardGameResponseDto(userBoardGame);
    }

    public void deleteUserBoardGame(long id) {
        throwExceptionWhenNotExistsById(id, userBoardGameRepository);
        userBoardGameRepository.deleteById(id);
    }
}
