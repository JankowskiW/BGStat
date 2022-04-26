package pl.wj.bgstat.attribute.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeRequestDto {
    private long objectTypeId;
    private long objectId;
    private long attributeClassId;
    private String value;
    private boolean multivaluedAttributeClassType;
}
