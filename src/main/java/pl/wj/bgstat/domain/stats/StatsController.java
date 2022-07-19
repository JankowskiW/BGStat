package pl.wj.bgstat.domain.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.stats.model.dto.StatsGameplaysResponseDto;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {

    private static final String MIN_DATE = "1990-01-01";
    private static final String MAX_DATE = "2999-12-31";

    private final StatsService statsService;

    @GetMapping("/gameplays")
    public StatsGameplaysResponseDto getGameplaysStats(
            @RequestParam(required = false, defaultValue = MIN_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = MAX_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return statsService.getGameplaysStats(fromDate, toDate);
    }

    @GetMapping("/gameplays/users/{id}")
    public StatsGameplaysResponseDto getGameplaysStatsOfGivenUser(
            @PathVariable long id,
            @RequestParam(required = false, defaultValue = MIN_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = MAX_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(required = false) Long boardGameId) {
        return statsService.getGameplaysStatsOfGivenUser(id, fromDate, toDate, boardGameId);
    }

    @GetMapping("/gameplays/board-games/{id}")
    public StatsGameplaysResponseDto getGameplaysStatsOfGivenBoardGame(
            @PathVariable long id,
            @RequestParam(required = false, defaultValue = MIN_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = MAX_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return statsService.getGameplaysStatsOfGivenBoardGame(id, fromDate, toDate);
    }

}
