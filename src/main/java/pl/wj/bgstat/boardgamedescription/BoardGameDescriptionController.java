package pl.wj.bgstat.boardgamedescription;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board_game_description")
public class BoardGameDescriptionController {

    private final BoardGameDescriptionService boardGameDescriptionService;

    @PutMapping("/{id}")
    public BoardGameDescriptionResponseDto editBoardGameDescription(@PathVariable Long id,
                          @RequestBody @Valid BoardGameDescriptionRequestDto boardGameRequestDescriptionDto) {
        return boardGameDescriptionService.editBoardGameDescription(id, boardGameRequestDescriptionDto);
    }


}
