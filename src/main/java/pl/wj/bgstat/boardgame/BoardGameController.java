package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board_games")
public class BoardGameController {

    private static final int PAGE_SIZE = 25;

    private final BoardGameService boardGameService;



    @GetMapping("")
    public List<BoardGameHeaderDto> getBoardGameHeaders(@RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size) {
        int pageNumber = (page != null && page > 0 ? page : 1)  - 1;
        int pageSize = size != null && size >= 0 ? size : PAGE_SIZE;
        return boardGameService.getBoardGameHeaders(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public BoardGameResponseDto getSingleBoardGame(@PathVariable Long id) {
        return boardGameService.getSingleBoardGame(id);
    }

    @PostMapping("")
    public BoardGameResponseDto addBoardGame(@RequestBody @Valid BoardGameRequestDto boardGameRequestDto) {
        return boardGameService.addBoardGame(boardGameRequestDto);
    }

    @PutMapping("/{id}")
    public BoardGameResponseDto editBoardGame(@PathVariable Long id, @RequestBody @Valid BoardGameRequestDto boardGameRequestDto) {
        return boardGameService.editBoardGame(id, boardGameRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBoardGame(@PathVariable Long id) {
        boardGameService.deleteBoardGame(id);
    }



}

