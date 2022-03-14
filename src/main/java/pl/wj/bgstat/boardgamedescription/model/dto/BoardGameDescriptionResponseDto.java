package pl.wj.bgstat.boardgamedescription.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardGameDescriptionResponseDto {
    private Long id;
    private String description;
}
