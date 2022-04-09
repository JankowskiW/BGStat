package pl.wj.bgstat.systemobjecttype.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemObjectTypeRequestDto {
    @NotBlank @Length(max = 100)
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private boolean archived;
}
