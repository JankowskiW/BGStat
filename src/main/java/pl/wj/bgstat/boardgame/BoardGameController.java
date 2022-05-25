package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-games")
public class BoardGameController {

    private static final String MIN_DATE = "1990-01-01";
    private static final String MAX_DATE = "2999-12-31";

    private final BoardGameService boardGameService;

    @GetMapping("")
    public Page<BoardGameHeaderDto> getBoardGameHeaders(Pageable pageable) {
        return boardGameService.getBoardGameHeaders(pageable);
    }

    @GetMapping("/{id}")
    public BoardGameResponseDto getSingleBoardGame(@PathVariable long id) {
        return boardGameService.getSingleBoardGame(id);
    }

    @PostMapping("")
    public BoardGameResponseDto addBoardGame(@RequestBody @Valid BoardGameRequestDto boardGameRequestDto) {
        return boardGameService.addBoardGame(boardGameRequestDto);
    }

    @PutMapping("/{id}")
    public BoardGameResponseDto editBoardGame(@PathVariable long id, @RequestBody @Valid BoardGameRequestDto boardGameRequestDto) {
        return boardGameService.editBoardGame(id, boardGameRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBoardGame(@PathVariable long id) {
        boardGameService.deleteBoardGame(id);
    }

    @GetMapping("/{id}/stats")
    public BoardGameGameplaysStatsDto getBoardGameStats(
            @PathVariable long id,
            @RequestParam(required = false, defaultValue = MIN_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = MAX_DATE) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return boardGameService.getBoardGameStats(id, fromDate, toDate);
    }
}

