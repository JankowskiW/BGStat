package pl.wj.bgstat.boardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardGameHeaderDto {
    private long id;
    private String name;
    //private String thumbnailPath;
}
