package pl.wj.bgstat.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.gameplay.model.dto.GameplayRequestDto;
import pl.wj.bgstat.gameplay.model.dto.GameplayResponseDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import javax.validation.Valid;
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

    @PostMapping("")
    public GameplayResponseDto addGameplay(@RequestBody @Valid GameplayRequestDto gameplayRequestDto) {
        return gameplayService.addGameplay(gameplayRequestDto);
    }

    @PutMapping("/{id}")
    public GameplayResponseDto editGameplay(@PathVariable long id, @RequestBody GameplayRequestDto gameplayRequestDto) {
        return gameplayService.editGameplay(id, gameplayRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGameplay(@PathVariable long id) {gameplayService.deleteGameplay(id);}

}
