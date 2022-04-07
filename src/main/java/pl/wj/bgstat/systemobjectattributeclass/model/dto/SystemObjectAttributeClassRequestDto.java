package pl.wj.bgstat.systemobjectattributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemObjectAttributeClassRequestDto {
    private long attributeClassId;
    private long systemObjectTypeId;
    private boolean required;
    private String classDefaultValue;
}
