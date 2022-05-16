package pl.wj.bgstat.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/user-board_games")
    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(@PathVariable long id, Pageable pageable) {
        return userService.getUserBoardGameHeaders(id, pageable);
    }

}
