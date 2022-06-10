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
    public BoardGameResponseDto addBoardGame(@RequestPart @Valid BoardGameRequestDto boardGameRequestDto,
                                             @RequestPart("thumbnail") MultipartFile thumbnail) {
        return boardGameService.addBoardGame(boardGameRequestDto, thumbnail);
    }

    @PutMapping("/{id}")
    public BoardGameResponseDto editBoardGame(@PathVariable long id, @RequestBody @Valid BoardGameRequestDto boardGameRequestDto) {
        return boardGameService.editBoardGame(id, boardGameRequestDto);
    }

    @PostMapping("/{id}/thumbnail")
    public BoardGameThumbnailResponseDto addThumbnail(@PathVariable long id, @RequestPart("thumbnail") MultipartFile thumbnail) {
        return boardGameService.addOrReplaceThumbnail(id, thumbnail);
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

