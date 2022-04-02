package pl.wj.bgstat.systemobjecttype;

import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;

import java.util.ArrayList;
import java.util.List;

public class SystemObjectTypeServiceTestHelper {

    public static List<SystemObjectType> populateSystemObjectTypeList(int numberOfElements) {
        List<SystemObjectType> systemObjectTypeList = new ArrayList<>();
        SystemObjectType systemObjectType;
        for (int i = 1; i <= numberOfElements; i++) {
            systemObjectType = new SystemObjectType();
            systemObjectType.setId(i);
            systemObjectType.setName("Name No. " + i);
            systemObjectType.setDescription("DESCRIPTION OF " + systemObjectType.getName());
            systemObjectType.setArchivized(false);
            systemObjectTypeList.add(systemObjectType);
        }
        return systemObjectTypeList;
    }

    public static List<SystemObjectTypeHeaderDto> populateSystemObjectTypeHeaderDtoList(List<SystemObjectType> systemObjectTypeList) {
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaderDtoList = new ArrayList<>();
        for(SystemObjectType systemObjectType : systemObjectTypeList) {
            systemObjectTypeHeaderDtoList.add(new SystemObjectTypeHeaderDto(
                   systemObjectType.getId(), systemObjectType.getName(), systemObjectType.isArchivized()));
        }
        return systemObjectTypeHeaderDtoList;
    }

    public static SystemObjectTypeRequestDto createSystemObjectTypeRequestDto(int currentSize) {
        return new SystemObjectTypeRequestDto(
                "Name No. " + (currentSize + 1),
                "DESCRIPTION OF Name No. " + (currentSize + 1),
                false);
    }
}
