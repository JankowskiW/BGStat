package pl.wj.bgstat.attributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeClassResponseDto {
    private long id;
    private String name;
    private String description;
    private long attributeClassTypeId;
    private String attributeClassTypeName;
}