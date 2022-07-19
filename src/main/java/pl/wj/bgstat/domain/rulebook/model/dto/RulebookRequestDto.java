package pl.wj.bgstat.domain.rulebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.domain.rulebook.enumeration.LanguageISO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RulebookRequestDto {
    @NotNull(message = "Board game id cannot be null")
    @Min(value = 1, message = "Board game id should be positive integer number")
    private long boardGameId;
    @NotNull(message = "Language ISO code cannot be null")
    private LanguageISO languageIso;
}
