package pl.wj.bgstat.domain.systemobjecttype.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemObjectTypeResponseDto {
    private long id;
    private String name;
    private String description;
    private boolean archived;
}
