package pl.wj.bgstat.domain.attribute.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeResponseDto {
    private long id;
    private long objectTypeId;
    private long objectId;
    private long attributeClassId;
    private String value;
}
