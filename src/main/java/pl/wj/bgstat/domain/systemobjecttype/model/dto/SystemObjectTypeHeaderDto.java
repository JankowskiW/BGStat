package pl.wj.bgstat.domain.systemobjecttype.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SystemObjectTypeHeaderDto {
    private long id;
    private String name;
    private boolean archived;
}
