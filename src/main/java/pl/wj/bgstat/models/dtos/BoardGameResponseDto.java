package pl.wj.bgstat.models.dtos;

import lombok.Builder;
import lombok.Getter;

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
