package pl.wj.bgstat.rulebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RulebookRequestDto {
    @NotNull @Min(1)
    private long boardGameId;
    private LanguageISO languageIso;
}
