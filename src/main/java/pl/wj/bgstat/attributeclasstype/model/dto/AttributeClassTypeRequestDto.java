package pl.wj.bgstat.attributeclasstype.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeClassTypeRequestDto {
    private String name;
    private String description;
    private boolean archived;
}
