package pl.wj.bgstat.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gameplays")
public class GameplayController {

    private final GameplayService gameplayService;

    @GetMapping("/stats")
    public GameplaysStatsDto getGameplayStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return gameplayService.getGameplayStats(fromDate, toDate);
    }
}
