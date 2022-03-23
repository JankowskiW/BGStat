package pl.wj.bgstat.boardgame.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardGameMapper {

    public static BoardGame mapToBoardGame(long id, BoardGameRequestDto boardGameRequestDto) {
        BoardGame boardGame = mapToBoardGame(boardGameRequestDto);
        boardGame.setId(id);
        boardGame.getBoardGameDescription().setBoardGameId(id);
        return boardGame;
    }

    public static BoardGame mapToBoardGame(BoardGameRequestDto boardGameRequestDto) {
        BoardGame boardGame = new BoardGame();
        boardGame.setName(boardGameRequestDto.getName());
        boardGame.setPlayingTime(boardGameRequestDto.getPlayingTime());
        boardGame.setRecommendedAge(boardGameRequestDto.getRecommendedAge());
        boardGame.setMinPlayersNumber(boardGameRequestDto.getMinPlayersNumber());
        boardGame.setMaxPlayersNumber(boardGameRequestDto.getMaxPlayersNumber());
        boardGame.setComplexity(boardGameRequestDto.getComplexity());
        boardGame.setBoardGameDescription(new BoardGameDescription());
        boardGame.getBoardGameDescription().setDescription(boardGameRequestDto.getDescription());
        boardGame.getBoardGameDescription().setBoardGame(boardGame);
        return boardGame;
    }

    public static List<BoardGameHeaderDto> mapToBoardGameHeaderDtos(List<BoardGame> boardGames) {
        return boardGames.stream()
                .map(boardGame -> mapToBoardGameHeaderDto(boardGame))
                .collect(Collectors.toList());
    }

    private static BoardGameHeaderDto mapToBoardGameHeaderDto(BoardGame boardGame) {
        return BoardGameHeaderDto.builder()
                .id(boardGame.getId())
                .name(boardGame.getName())
                .build();
    }

    public static BoardGameResponseDto mapToBoardGameResponseDto(BoardGame boardGame) {
        return BoardGameResponseDto.builder()
                .id(boardGame.getId())
                .name(boardGame.getName())
                .playingTime(boardGame.getPlayingTime())
                .recommendedAge(boardGame.getRecommendedAge())
                .minPlayersNumber(boardGame.getMinPlayersNumber())
                .maxPlayersNumber(boardGame.getMaxPlayersNumber())
                .complexity(boardGame.getComplexity())
                .description(boardGame.getBoardGameDescription() != null ? boardGame.getBoardGameDescription().getDescription() : "")
                .build();
    }
}
