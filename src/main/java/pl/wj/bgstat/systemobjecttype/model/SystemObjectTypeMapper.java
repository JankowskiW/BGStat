package pl.wj.bgstat.systemobjecttype.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;

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
        return systemObjectType;
    }
}
