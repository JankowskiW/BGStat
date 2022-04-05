package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.BOARD_GAME_NOT_FOUND_EX_MSG;
import static pl.wj.bgstat.exception.ExceptionHelper.BOARD_GAME_TYPE_EXISTS_EX_MSG;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    public Page<BoardGameHeaderDto> getBoardGameHeaders(Pageable pageable) {
        return boardGameRepository.findAllBoardGameHeaders(pageable);
    }
    
    public BoardGameResponseDto getSingleBoardGame(long id) {
        BoardGame boardGame = boardGameRepository.findWithDescriptionById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOARD_GAME_NOT_FOUND_EX_MSG + id));
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto) {
        if (boardGameRepository.existsByName(boardGameRequestDto.getName()))
            throw new EntityExistsException(BOARD_GAME_TYPE_EXISTS_EX_MSG);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto editBoardGame(long id, BoardGameRequestDto boardGameRequestDto) {
        if (!boardGameRepository.existsById(id)) throw new EntityNotFoundException(BOARD_GAME_NOT_FOUND_EX_MSG + id);
        if (boardGameRepository.existsByNameAndIdNot(boardGameRequestDto.getName(), id))
            throw new EntityExistsException(BOARD_GAME_TYPE_EXISTS_EX_MSG);

        BoardGame boardGame = BoardGameMapper.mapToBoardGame(id, boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public void deleteBoardGame(long id) {
        if (!boardGameRepository.existsById(id)) throw new EntityNotFoundException(BOARD_GAME_NOT_FOUND_EX_MSG + id);
        boardGameRepository.deleteById(id);
    }
}
