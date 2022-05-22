package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private static final long BOARD_GAME_DEFAULT_OBJECT_TYPE_ID = 1L;

    private final BoardGameRepository boardGameRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;

    public Page<BoardGameHeaderDto> getBoardGameHeaders(Pageable pageable) {
        return boardGameRepository.findBoardGameHeaders(pageable);
    }
    
    public BoardGameResponseDto getSingleBoardGame(long id) {
        BoardGame boardGame = boardGameRepository.findWithDescriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto) {
        boardGameRequestDto.setObjectTypeId(validateSystemObjectTypeId(boardGameRequestDto.getObjectTypeId()));
        throwExceptionWhenExistsByName(boardGameRequestDto.getName());
        throwExceptionWhenNotExistsById(boardGameRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto editBoardGame(long id, BoardGameRequestDto boardGameRequestDto) {
        boardGameRequestDto.setObjectTypeId(validateSystemObjectTypeId(boardGameRequestDto.getObjectTypeId()));
        throwExceptionWhenNotExistsById(id, boardGameRepository);
        throwExceptionWhenExistsByNameAndNotId(id, boardGameRequestDto.getName());
        throwExceptionWhenNotExistsById(boardGameRequestDto.getObjectTypeId(), systemObjectTypeRepository);

        BoardGame boardGame = BoardGameMapper.mapToBoardGame(id, boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public void deleteBoardGame(long id) {
        throwExceptionWhenNotExistsById(id, boardGameRepository);
        boardGameRepository.deleteById(id);
    }


    private void throwExceptionWhenExistsByName(String name) {
        if (boardGameRepository.existsByName(name))
            throw new ResourceExistsException(BOARD_GAME_RESOURCE_NAME, NAME_FIELD);
    }

    private void throwExceptionWhenExistsByNameAndNotId(long id, String name) {
        if (boardGameRepository.existsByNameAndIdNot(name, id))
            throw new ResourceExistsException(BOARD_GAME_RESOURCE_NAME, NAME_FIELD);
    }

    private long validateSystemObjectTypeId(long id) {
        return id == 0 ? BOARD_GAME_DEFAULT_OBJECT_TYPE_ID : id;
    }
}
