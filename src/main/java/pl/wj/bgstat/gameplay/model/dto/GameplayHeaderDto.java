package pl.wj.bgstat.gameplay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GameplayHeaderDto {
    private long id;
    private String boardGameName;
    private int playtime;
}
