package pl.wj.bgstat.domain.boardgamedescription;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.domain.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-game-descriptions")
public class BoardGameDescriptionController {

    private final BoardGameDescriptionService boardGameDescriptionService;

    @PutMapping("/{id}")
    public BoardGameDescriptionResponseDto editBoardGameDescription(@PathVariable long id,
                                                                    @RequestBody @Valid BoardGameDescriptionRequestDto boardGameRequestDescriptionDto) {
        return boardGameDescriptionService.editBoardGameDescription(id, boardGameRequestDescriptionDto);
    }


}
