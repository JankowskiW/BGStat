package pl.wj.bgstat.domain.systemobjecttype.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeRequestDto;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemObjectTypeMapper {

    public static SystemObjectType mapToSystemObjectType(long id, SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        SystemObjectType systemObjectType = mapToSystemObjectType(systemObjectTypeRequestDto);
        systemObjectType.setId(id);
        return systemObjectType;
    }

    public static SystemObjectType mapToSystemObjectType(SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        SystemObjectType systemObjectType = new SystemObjectType();
        systemObjectType.setName(systemObjectTypeRequestDto.getName());
        systemObjectType.setDescription(systemObjectTypeRequestDto.getDescription());
        systemObjectType.setArchived(systemObjectTypeRequestDto.isArchived());
        return systemObjectType;
    }

    public static SystemObjectTypeResponseDto mapToSystemObjectTypeResponseDto(SystemObjectType systemObjectType) {
        return SystemObjectTypeResponseDto.builder()
                .id(systemObjectType.getId())
                .name(systemObjectType.getName())
                .description(systemObjectType.getDescription())
                .archived(systemObjectType.isArchived())
                .build();

    }
}
