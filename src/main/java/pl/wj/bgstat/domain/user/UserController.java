package pl.wj.bgstat.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.gameplay.model.dto.GameplayHeaderDto;
import pl.wj.bgstat.domain.user.model.User;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameHeaderDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{id}/user-board-games")
    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(@PathVariable long id, Pageable pageable) {
        return userService.getUserBoardGameHeaders(id, pageable);
    }

    @GetMapping("/{id}/gameplays")
    public Page<GameplayHeaderDto> getUserGameplayHeaders(@PathVariable long id, Pageable pageable) {
        return userService.getUserGameplayHeaders(id, pageable);
    }

}
