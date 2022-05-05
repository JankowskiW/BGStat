package pl.wj.bgstat.boardgamedescription.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameDescriptionRequestDto {
    @NotBlank @Length(max = 1500)
    private String description;
}
