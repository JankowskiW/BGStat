package pl.wj.bgstat.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.gameplay.GameplayRepository;
import pl.wj.bgstat.gameplay.GameplayService;
import pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final GameplayRepository gameplayRepository;

    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(long id, Pageable pageable) {
        throwExceptionWhenNotExistsById(id, userRepository);
        return userBoardGameRepository.findUserBoardGameHeaders(id, pageable);
    }

    public Page<GameplayHeaderDto> getUserGameplayHeaders(long id, Pageable pageable) {
        throwExceptionWhenNotExistsById(id, userRepository);
        return gameplayRepository.findUserGameplayHeaders(id, pageable);
    }
}
