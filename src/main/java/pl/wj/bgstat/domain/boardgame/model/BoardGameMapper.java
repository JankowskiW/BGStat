package pl.wj.bgstat.domain.boardgame.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.domain.boardgame.model.dto.BoardGamePartialRequestDto;
import pl.wj.bgstat.domain.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.domain.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.domain.boardgamedescription.model.BoardGameDescription;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardGameMapper {

    public static BoardGame mapToBoardGame(BoardGame boardGame, BoardGamePartialRequestDto partialBg) {
        if (partialBg.getName() != null) boardGame.setName(partialBg.getName());
        if (partialBg.getRecommendedAge() != null) boardGame.setRecommendedAge(partialBg.getRecommendedAge());
        if (partialBg.getMinPlayersNumber() != null) boardGame.setMinPlayersNumber(partialBg.getMinPlayersNumber());
        if (partialBg.getMaxPlayersNumber() != null) boardGame.setMaxPlayersNumber(partialBg.getMaxPlayersNumber());
        if (partialBg.getComplexity() != null) boardGame.setComplexity(partialBg.getComplexity());
        if (partialBg.getEstimatedPlaytime() != null) boardGame.setEstimatedPlaytime(partialBg.getEstimatedPlaytime());
        if (partialBg.getDescription() != null) boardGame.getBoardGameDescription().setDescription(partialBg.getDescription());
        return boardGame;
    }

    public static BoardGame mapToBoardGame(long id, BoardGameRequestDto boardGameRequestDto) {
        BoardGame boardGame = mapToBoardGame(boardGameRequestDto);
        boardGame.setId(id);
        boardGame.getBoardGameDescription().setBoardGameId(id);
        return boardGame;
    }

    public static BoardGame mapToBoardGame(BoardGameRequestDto boardGameRequestDto) {
        BoardGame boardGame = new BoardGame();
        boardGame.setObjectTypeId(boardGameRequestDto.getObjectTypeId());
        boardGame.setName(boardGameRequestDto.getName());
        boardGame.setEstimatedPlaytime(boardGameRequestDto.getEstimatedPlaytime());
        boardGame.setRecommendedAge(boardGameRequestDto.getRecommendedAge());
        boardGame.setMinPlayersNumber(boardGameRequestDto.getMinPlayersNumber());
        boardGame.setMaxPlayersNumber(boardGameRequestDto.getMaxPlayersNumber());
        boardGame.setComplexity(boardGameRequestDto.getComplexity());
        boardGame.setBoardGameDescription(new BoardGameDescription());
        boardGame.getBoardGameDescription().setDescription(boardGameRequestDto.getDescription());
        boardGame.getBoardGameDescription().setBoardGame(boardGame);
        return boardGame;
    }

    public static BoardGameResponseDto mapToBoardGameResponseDto(BoardGame boardGame) {
        return BoardGameResponseDto.builder()
                .id(boardGame.getId())
                .objectTypeId(boardGame.getObjectTypeId())
                .name(boardGame.getName())
                .estimatedPlaytime(boardGame.getEstimatedPlaytime())
                .recommendedAge(boardGame.getRecommendedAge())
                .minPlayersNumber(boardGame.getMinPlayersNumber())
                .maxPlayersNumber(boardGame.getMaxPlayersNumber())
                .complexity(boardGame.getComplexity())
                .description(boardGame.getBoardGameDescription() != null ? boardGame.getBoardGameDescription().getDescription() : "")
                .build();
    }
}
