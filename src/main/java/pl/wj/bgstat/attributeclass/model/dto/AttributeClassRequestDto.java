package pl.wj.bgstat.attributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeClassRequestDto {
    @NotBlank @Length(max = 50)
    private String name;
    @NotNull
    private String description;
    @NotNull @Min(1)
    private long attributeClassTypeId;
}
