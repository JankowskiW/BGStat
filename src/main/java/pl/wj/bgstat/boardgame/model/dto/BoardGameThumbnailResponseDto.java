package pl.wj.bgstat.boardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BoardGameThumbnailResponseDto {
    private long id;
    private String thumbnailPath;
}
