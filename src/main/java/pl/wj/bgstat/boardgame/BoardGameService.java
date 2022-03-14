package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    
    
    public List<BoardGameHeaderDto> getBoardGameHeaders(int pageNumber, int pageSize) {
        return boardGameRepository.findAllBoardGameHeaders(PageRequest.of(pageNumber, pageSize));
    }
    
    public BoardGameResponseDto getSingleBoardGame(Long id) {
        BoardGame boardGame = boardGameRepository.findWithDescriptionById(id);
        if (boardGame == null) throw new EntityNotFoundException("No such board game with id: " + id);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto) {
        if (boardGameRepository.existsByName(boardGameRequestDto.getName()))
            throw new EntityExistsException("Board game with name '" +
                    boardGameRequestDto.getName() + "' already exists in database");
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGameRepository.save(boardGame);
        BoardGameResponseDto boardGameResponseDto = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        return boardGameResponseDto;
    }

    public BoardGameResponseDto editBoardGame(Long id, BoardGameRequestDto boardGameRequestDto) {
        if (!boardGameRepository.existsById(id)) throw new EntityNotFoundException("No such board game with id: " + id);
        if (boardGameRepository.existsByNameAndIdNot(boardGameRequestDto.getName(), id))
            throw new EntityExistsException("Board game with name '" +
                    boardGameRequestDto.getName() + "' already exists in database");

        BoardGame boardGame = BoardGameMapper.mapToBoardGame(id, boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public void deleteBoardGame(Long id) {
        boardGameRepository.deleteById(id);
    }
}
