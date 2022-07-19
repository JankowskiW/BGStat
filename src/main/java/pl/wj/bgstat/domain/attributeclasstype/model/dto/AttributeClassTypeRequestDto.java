package pl.wj.bgstat.domain.attributeclasstype.model.dto;

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
    @NotBlank(message = "Attribute class type name cannot be blank")
    @Length(max = 50, message = "Attribute class type name cannot be longer than 50 characters")
    private String name;
    @NotNull(message = "Attribute class type description cannot be null")
    @Length(max = 255, message = "Attribute class type name cannot be longer than 255 characters")
    private String description;
    private boolean archived;
    private boolean multivalued;
}
