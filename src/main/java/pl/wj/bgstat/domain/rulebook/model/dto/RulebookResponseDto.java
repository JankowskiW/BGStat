package pl.wj.bgstat.domain.rulebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.domain.rulebook.enumeration.LanguageISO;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RulebookResponseDto {
    private long id;
    private long boardGameId;
    private LanguageISO languageIso;
    private String path;
}
