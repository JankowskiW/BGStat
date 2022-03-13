package pl.wj.bgstat.boardgame.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class BoardGameResponseDto {
    private long id;
    private String name;
    private int recommendedAge;
    private int minPlayersNumber;
    private int maxPlayersNumber;
    private int complexity;
    private int playingTime;
    private String description;
}
