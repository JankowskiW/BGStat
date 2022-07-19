package pl.wj.bgstat.domain.systemobjectattributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemObjectAttributeClassRequestDto {
    @NotNull(message = "Attribute class id cannot be null")
    @Min(value=1, message = "Attribute class id should be positive integer number")
    private long attributeClassId;
    @NotNull(message = "System object type id cannot be null")
    @Min(value=1, message = "System object type id should be positive integer number")
    private long systemObjectTypeId;
    @NotNull(message = "Required field cannot be null")
    private boolean required;
    @Length(max = 150, message = "Class default value cannot be longer than 150 characters")
    private String classDefaultValue;
}
