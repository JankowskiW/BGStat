package pl.wj.bgstat.boardgamedescription.model;

import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

public class BoardGameDescriptionMapper {

    public static BoardGameDescription mapToBoardGameDescription(Long id,
                                       BoardGameDescriptionRequestDto boardGameDescriptionRequestDto) {
        BoardGameDescription boardGameDescription = new BoardGameDescription();
        boardGameDescription.setBoardGameId(id);
        boardGameDescription.setDescription(boardGameDescriptionRequestDto.getDescription());
        return boardGameDescription;
    }

    public static BoardGameDescriptionResponseDto mapToBoardGameDescriptionResponseDto(BoardGameDescription boardGameDescription) {
        return BoardGameDescriptionResponseDto.builder()
                .id(boardGameDescription.getBoardGameId())
                .description(boardGameDescription.getDescription())
                .build();
    }
}
