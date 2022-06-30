package pl.wj.bgstat.rulebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
