package pl.wj.bgstat.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/user-board-games")
    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(@PathVariable long id, Pageable pageable) {
        return userService.getUserBoardGameHeaders(id, pageable);
    }

    @GetMapping("/{id}/gameplays")
    public Page<GameplayHeaderDto> getUserGameplayHeaders(@PathVariable long id, Pageable pageable) {
        return null;
    }

    @GetMapping("/{id}/gameplays/stats")
    public GameplaysStatsDto getUserGameplaysStats(
            @PathVariable long id, @RequestParam(required = false) long boardGameId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return null;
    }


}
