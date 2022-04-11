package pl.wj.bgstat.boardgamedescription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescriptionMapper;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class BoardGameDescriptionService {

    private final BoardGameDescriptionRepository boardGameDescriptionRepository;

    public BoardGameDescriptionResponseDto editBoardGameDescription(long id,
                      BoardGameDescriptionRequestDto boardGameRequestDescriptionDto) {
        BoardGameDescription boardGameDescription = boardGameDescriptionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOARD_GAME_DESCRIPTION_RESOURCE_NAME, ID_FIELD, id));
        boardGameDescription.setDescription(boardGameRequestDescriptionDto.getDescription());
        return BoardGameDescriptionMapper.mapToBoardGameDescriptionResponseDto(
                    boardGameDescriptionRepository.save(boardGameDescription));
    }
}
