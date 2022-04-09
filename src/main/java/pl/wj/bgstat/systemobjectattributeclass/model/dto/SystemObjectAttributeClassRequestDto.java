package pl.wj.bgstat.systemobjectattributeclass.model.dto;

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
    @NotNull @Min(1)
    private long attributeClassId;
    @NotNull @Min(1)
    private long systemObjectTypeId;
    @NotNull
    private boolean required;
    @Length(max = 150)
    private String classDefaultValue;
}
