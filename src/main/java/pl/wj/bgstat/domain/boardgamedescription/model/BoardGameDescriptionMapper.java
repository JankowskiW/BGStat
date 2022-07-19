package pl.wj.bgstat.domain.boardgamedescription.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.domain.boardgamedescription.model.dto.BoardGameDescriptionResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardGameDescriptionMapper {

    public static BoardGameDescriptionResponseDto mapToBoardGameDescriptionResponseDto(BoardGameDescription boardGameDescription) {
        return BoardGameDescriptionResponseDto.builder()
                .boardGameId(boardGameDescription.getBoardGameId())
                .description(boardGameDescription.getDescription())
                .build();
    }
}
