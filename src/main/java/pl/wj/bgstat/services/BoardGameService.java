package pl.wj.bgstat.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.mappers.BoardGameMapper;
import pl.wj.bgstat.models.BoardGame;
import pl.wj.bgstat.models.dtos.BoardGameHeaderDto;
import pl.wj.bgstat.models.dtos.BoardGameRequestDto;
import pl.wj.bgstat.models.dtos.BoardGameResponseDto;
import pl.wj.bgstat.repositories.BoardGameRepository;

import java.util.List;

import static pl.wj.bgstat.mappers.BoardGameMapper.mapToBoardGameHeaderDtos;

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
