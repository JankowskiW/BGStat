package pl.wj.bgstat.rulebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RulebookResponseDto {
    private long id;
    private long boardGameId;
    private String languageIso;
    private String path;
}
