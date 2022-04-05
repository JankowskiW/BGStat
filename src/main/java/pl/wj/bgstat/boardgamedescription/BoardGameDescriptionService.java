package pl.wj.bgstat.boardgamedescription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescriptionMapper;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

import javax.persistence.EntityNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.BOARD_GAME_DESCRIPTION_NOT_FOUND_EX_MSG;

@Service
@RequiredArgsConstructor
public class BoardGameDescriptionService {

    private final BoardGameDescriptionRepository boardGameDescriptionRepository;

    public BoardGameDescriptionResponseDto editBoardGameDescription(long id,
                      BoardGameDescriptionRequestDto boardGameRequestDescriptionDto) {
        BoardGameDescription boardGameDescription = boardGameDescriptionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(BOARD_GAME_DESCRIPTION_NOT_FOUND_EX_MSG));
        boardGameDescription.setDescription(boardGameRequestDescriptionDto.getDescription());
        return BoardGameDescriptionMapper.mapToBoardGameDescriptionResponseDto(
                    boardGameDescriptionRepository.save(boardGameDescription));
    }
}
