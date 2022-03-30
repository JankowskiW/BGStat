package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board_games")
public class BoardGameController {

    private static final int PAGE_SIZE = 25;

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



}

