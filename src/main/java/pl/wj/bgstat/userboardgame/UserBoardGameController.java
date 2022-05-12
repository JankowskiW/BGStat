package pl.wj.bgstat.userboardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-board-games")
public class UserBoardGameController {

    private final UserBoardGameService userBoardGameService;

    @GetMapping("/{id}")
    public UserBoardGameDetailsDto getSingleUserBoardGame(@PathVariable long id) {
        return userBoardGameService.getSingleUserBoardGame(id);
    }

    @PostMapping("")
    public UserBoardGameResponseDto addUserBoardGame(@RequestBody @Valid UserBoardGameRequestDto userBoardGameRequestDto) {
        return userBoardGameService.addUserBoardGame(userBoardGameRequestDto);
    }

    @PutMapping("/{id}")
    public UserBoardGameResponseDto editUserBoardGame(
            @PathVariable long id, UserBoardGameRequestDto userBoardGameRequestDto) {
        return userBoardGameService.editUserBoardGame(id, userBoardGameRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserBoardGame(@PathVariable long id) {
        userBoardGameService.deleteUserBoardGame(id);
    }
}
