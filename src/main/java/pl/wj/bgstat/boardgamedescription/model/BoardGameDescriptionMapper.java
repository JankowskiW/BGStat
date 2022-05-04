package pl.wj.bgstat.boardgamedescription.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionRequestDto;
import pl.wj.bgstat.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardGameDescriptionMapper {

    public static BoardGameDescriptionResponseDto mapToBoardGameDescriptionResponseDto(BoardGameDescription boardGameDescription) {
        return BoardGameDescriptionResponseDto.builder()
                .boardGameId(boardGameDescription.getBoardGameId())
                .description(boardGameDescription.getDescription())
                .build();
    }
}
