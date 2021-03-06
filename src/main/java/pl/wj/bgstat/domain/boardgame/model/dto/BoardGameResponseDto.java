package pl.wj.bgstat.domain.boardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BoardGameResponseDto {
    private long id;
    private long objectTypeId;
    private String name;
    private int recommendedAge;
    private int minPlayersNumber;
    private int maxPlayersNumber;
    private int complexity;
    private int estimatedPlaytime;
    private String description;
}
