package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.dtos.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dtos.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dtos.BoardGameResponseDto;
import pl.wj.bgstat.boardgame.BoardGameRepository;

import java.util.List;
import java.util.Map;

import static pl.wj.bgstat.boardgame.model.BoardGameMapper.mapToBoardGameHeaderDtos;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    
    
    public List<BoardGameHeaderDto> getBoardGameHeaders(int pageNumber, int pageSize) {
        return boardGameRepository.findAllBoardGameHeaders(PageRequest.of(pageNumber, pageSize));
    }
    
    public BoardGameResponseDto getSingleBoardGame(Long id) {
        return BoardGameMapper.mapToBoardGameResponseDto(boardGameRepository.findWithDescriptionById(id));
    }

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto) {
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGameRepository.save(boardGame);
        BoardGameResponseDto boardGameResponseDto = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        return boardGameResponseDto;
    }

    public void deleteBoardGame(Long id) {
        boardGameRepository.deleteById(id);
    }

    public BoardGameResponseDto editBoardGame(Long id, BoardGameRequestDto boardGameRequestDto) {
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(id, boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto editPartOfBoardGame(Long id, Map<String, Object> partialBoardGameRequest) {
        BoardGame boardGame = boardGameRepository.findById(id).orElseThrow();
        boardGame = BoardGameMapper.mapToBoardGame(id, boardGame, partialBoardGameRequest);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }
}
