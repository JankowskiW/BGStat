package pl.wj.bgstat.attributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AttributeClassHeaderDto {
    private long id;
    private String name;
}
