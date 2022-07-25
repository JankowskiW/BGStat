package pl.wj.bgstat.domain.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.domain.boardgame.model.dto.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-games")
public class BoardGameController {
    private final BoardGameService boardGameService;

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public Page<BoardGameHeaderDto> getBoardGameHeaders(Pageable pageable) {
        return boardGameService.getBoardGameHeaders(pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public BoardGameResponseDto getSingleBoardGame(@PathVariable long id) {
        return boardGameService.getSingleBoardGame(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOMAIN_ADMIN', 'USER')")
    @PostMapping("")
    public BoardGameResponseDto addBoardGame(@RequestPart @Valid BoardGameRequestDto boardGameRequestDto,
                                             @RequestPart("thumbnail") MultipartFile thumbnail) {
        return boardGameService.addBoardGame(boardGameRequestDto, thumbnail);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOMAIN_ADMIN', 'USER')")
    @PutMapping("/{id}")
    public BoardGameResponseDto editBoardGame(@PathVariable long id,
                                              @RequestPart @Valid BoardGameRequestDto boardGameRequestDto,
                                              @RequestPart("thumbnail") MultipartFile thumbnail) {
        return boardGameService.editBoardGame(id, boardGameRequestDto, thumbnail);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOMAIN_ADMIN', 'USER')")
    @PostMapping("/{id}/thumbnail")
    public BoardGameThumbnailResponseDto addThumbnail(@PathVariable long id, @RequestPart("thumbnail") MultipartFile thumbnail) {
        return boardGameService.addOrReplaceThumbnail(id, thumbnail);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOMAIN_ADMIN', 'USER')")
    @PatchMapping("/{id}")
    public BoardGameResponseDto editBoardGamePartially(
            @PathVariable long id, @Valid @RequestBody BoardGamePartialRequestDto boardGamePartialRequestDto) {
        return boardGameService.editBoardGamePartially(id, boardGamePartialRequestDto);
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_DOMAIN_ADMIN', 'ROLE_USER')")
    @PreAuthorize("hasAuthority('BOARD_GAME_WRITE')")
    @DeleteMapping("/{id}")
    public void deleteBoardGame(@PathVariable long id) {
        boardGameService.deleteBoardGame(id);
    }
}

