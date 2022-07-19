package pl.wj.bgstat.domain.userboardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameResponseDto;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-board-games")
public class UserBoardGameController {

    private final UserBoardGameService userBoardGameService;

    @GetMapping("")
    public Page<UserBoardGameHeaderDto> getAuthorizedUserBoardGame(
            @AuthenticationPrincipal @ApiIgnore String username,
            Pageable pageable) {
        return userBoardGameService.getAuthorizedUserBoardGame(pageable, username);
    }

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
            @PathVariable long id, @RequestBody @Valid UserBoardGameRequestDto userBoardGameRequestDto) {
        return userBoardGameService.editUserBoardGame(id, userBoardGameRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserBoardGame(@PathVariable long id) {
        userBoardGameService.deleteUserBoardGame(id);
    }
}
