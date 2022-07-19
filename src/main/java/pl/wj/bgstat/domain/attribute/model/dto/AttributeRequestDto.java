package pl.wj.bgstat.domain.attribute.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeRequestDto {
    @Min(value = 1, message = "Attribute object type id should be positive integer number")
    private long objectTypeId;
    @Min(value = 1, message = "Attribute object id should be positive integer number")
    private long objectId;
    @Min(value = 1, message = "Attribute class id should be positive integer number")
    private long attributeClassId;
    @NotBlank(message = "Attribute value cannot be blank")
    @Length(max = 255, message = "Attribute value cannot be longer than 255 characters")
    private String value;
}
