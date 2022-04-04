package pl.wj.bgstat.attributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeClassRequestDto {
    private long attributeClassTypeId;
    private String name;
    private String description;
}
