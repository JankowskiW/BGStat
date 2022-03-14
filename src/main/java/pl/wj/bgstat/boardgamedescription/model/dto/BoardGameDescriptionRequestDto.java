package pl.wj.bgstat.boardgamedescription.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameDescriptionRequestDto {
    @NotBlank
    private String description;
}
