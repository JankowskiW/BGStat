package pl.wj.bgstat.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.stats.model.dto.StatsGameplaysRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/gameplays")
    public StatsGameplaysRequestDto getGameplaysStats(@RequestBody StatsGameplaysRequestDto statsGameplaysRequestDto) {
        return statsGameplaysRequestDto;
    }
}
