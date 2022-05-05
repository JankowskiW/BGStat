package pl.wj.bgstat.userboardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-board-games")
public class UserBoardGameController {

    private final UserBoardGameService userBoardGameService;
}
