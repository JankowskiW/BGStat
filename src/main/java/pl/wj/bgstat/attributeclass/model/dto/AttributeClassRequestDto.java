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
    @NotBlank(message = "Attribute class name cannot be null")
    @Length(max = 50, message = "Attribute class name cannot be longer than 50 characters")
    private String name;
    @NotNull (message = "Attribute class description cannot be null")
    @Length(max = 255, message = "Attribute class description cannot be longer than 255 characters")
    private String description;
    @NotNull(message = "Attribute class type id cannot be null")
    @Min(value = 1, message = "Attribute class type id must be greater than 0")
    private long attributeClassTypeId;
}
