package pl.wj.bgstat.boardgamedescription.model;

import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

public class BoardGameDescriptionMapper {

    public static BoardGameDescription mapToBoardGameDescription(long id,
                                       BoardGameDescriptionRequestDto boardGameDescriptionRequestDto) {
        BoardGameDescription boardGameDescription = new BoardGameDescription();
        boardGameDescription.setBoardGameId(id);
        boardGameDescription.setDescription(boardGameDescriptionRequestDto.getDescription());
        return boardGameDescription;
    }

    public static BoardGameDescriptionResponseDto mapToBoardGameDescriptionResponseDto(BoardGameDescription boardGameDescription) {
        return BoardGameDescriptionResponseDto.builder()
                .boardGameId(boardGameDescription.getBoardGameId())
                .description(boardGameDescription.getDescription())
                .build();
    }
}
