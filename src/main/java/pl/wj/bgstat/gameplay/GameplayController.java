package pl.wj.bgstat.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gameplays")
public class GameplayController {

    private final GameplayService gameplayService;

//    @GetMapping("/stats/gameplays-count")
//    public
}
