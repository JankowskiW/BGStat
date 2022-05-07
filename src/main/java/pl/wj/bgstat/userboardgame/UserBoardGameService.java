package pl.wj.bgstat.userboardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;

    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(Pageable pageable) {
        return userBoardGameRepository.findUserBoardGameHeaders(pageable);
    }
}
