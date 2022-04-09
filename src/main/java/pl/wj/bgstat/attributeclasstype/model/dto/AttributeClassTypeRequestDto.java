package pl.wj.bgstat.attributeclasstype.model.dto;

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
public class AttributeClassTypeRequestDto {
    @NotBlank @Length(max = 50)
    private String name;
    @NotNull
    private String description;
    @NotNull
    private boolean archived;
}
