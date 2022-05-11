package pl.wj.bgstat.userboardgame;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;

    public UserBoardGameDetailsDto getSingleUserBoardGame(long id) {
        throw new NotYetImplementedException();
    }

    public UserBoardGameResponseDto addUserBoardGame(UserBoardGameRequestDto userBoardGameRequestDto) {
        throw new NotYetImplementedException();
    }

    public UserBoardGameResponseDto editUserBoardGame(long id, UserBoardGameRequestDto userBoardGameRequestDto) {
        throw new NotYetImplementedException();
    }

    public void deleteUserBoardGame(long id) {
        throw new NotYetImplementedException();
    }
}
