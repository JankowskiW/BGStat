package pl.wj.bgstat.rulebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
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
    @Length(min = 2, max = 2)
    private LanguageISO languageIso;
}
