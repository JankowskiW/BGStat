package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.model.dto.*;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-games")
public class BoardGameController {

    private static final String MIN_DATE = "1990-01-01";
    private static final String MAX_DATE = "2999-12-31";

    private final BoardGameService boardGameService;

    @PostMapping("/thumbnail")
    public String addThumbnail(@RequestPart("file") MultipartFile file) {
        return "OK";
    }

    @GetMapping("")
    public Page<BoardGameHeaderDto> getBoardGameHeaders(Pageable pageable) {
        return boardGameService.getBoardGameHeaders(pageable);
    }

    @GetMapping("/{id}")
    public BoardGameResponseDto getSingleBoardGame(@PathVariable long id) {
        return boardGameService.getSingleBoardGame(id);
    }

    @PostMapping("")
    public BoardGameResponseDto addBoardGame(@RequestPart @Valid BoardGameRequestDto boardGameRequestDto,
                                             @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException, HttpMediaTypeNotSupportedException {
        return boardGameService.addBoardGame(boardGameRequestDto, thumbnail);
    }

    @PutMapping("/{id}")
    public BoardGameResponseDto editBoardGame(@PathVariable long id, @RequestBody @Valid BoardGameRequestDto boardGameRequestDto) {
        return boardGameService.editBoardGame(id, boardGameRequestDto);
    }

    @PatchMapping("/{id}")
    public BoardGameResponseDto editBoardGamePartially(
            @PathVariable long id, @Valid @RequestBody BoardGamePartialRequestDto boardGamePartialRequestDto) {
        return boardGameService.editBoardGamePartially(id, boardGamePartialRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBoardGame(@PathVariable long id) {
        boardGameService.deleteBoardGame(id);
    }
}

