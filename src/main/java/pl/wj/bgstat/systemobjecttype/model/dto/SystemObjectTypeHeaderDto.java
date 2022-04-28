package pl.wj.bgstat.systemobjecttype.model.dto;

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
