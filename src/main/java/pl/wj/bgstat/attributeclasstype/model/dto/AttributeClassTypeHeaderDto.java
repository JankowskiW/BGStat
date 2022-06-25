package pl.wj.bgstat.attributeclasstype.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AttributeClassTypeHeaderDto {
    private long id;
    private String name;
    private boolean archived;
    private boolean multivalued;
}
