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

import static pl.wj.bgstat.boardgame.model.BoardGameMapper.mapToBoardGameHeaderDtos;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;


    public List<BoardGameHeaderDto> getBoardGameHeaders(int pageNumber, int pageSize) {
        List<BoardGame> boardGames = boardGameRepository.findAllBoardGames(PageRequest.of(pageNumber, pageSize));
        return mapToBoardGameHeaderDtos(boardGames);
    }

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto) {
//        if (boardGameRepository.existsByName(boardGameRequestDto.getName()))
//            throw new BoardGameAlreadyExistsException(boardGameRequestDto.getName());
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        boardGameRepository.save(boardGame);
        BoardGameResponseDto boardGameResponseDto = BoardGameMapper.mapToBoardGameResponseDto(boardGame);
        return boardGameResponseDto;
    }
}
