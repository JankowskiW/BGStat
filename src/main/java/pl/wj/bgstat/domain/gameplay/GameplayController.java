package pl.wj.bgstat.domain.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.gameplay.model.dto.GameplayRequestDto;
import pl.wj.bgstat.domain.gameplay.model.dto.GameplayResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gameplays")
public class GameplayController {

    private final GameplayService gameplayService;

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
