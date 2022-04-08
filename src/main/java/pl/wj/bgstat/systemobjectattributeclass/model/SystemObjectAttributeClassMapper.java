package pl.wj.bgstat.systemobjectattributeclass.model;

import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;

public class SystemObjectAttributeClassMapper {

    public static SystemObjectAttributeClass mapToSystemObjectAttributeClass(
            SystemObjectAttributeClassId id,
            SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        SystemObjectAttributeClass systemObjectAttributeClass = new SystemObjectAttributeClass();
        systemObjectAttributeClass.setId(id);
        systemObjectAttributeClass.setRequired(systemObjectAttributeClass.isRequired());
        systemObjectAttributeClass.setAttributeClass(new AttributeClass());
        systemObjectAttributeClass.getAttributeClass().setId(systemObjectAttributeClassRequestDto.getAttributeClassId());
        systemObjectAttributeClass.setSystemObjectType(new SystemObjectType());
        systemObjectAttributeClass.getSystemObjectType().setId(systemObjectAttributeClassRequestDto.getSystemObjectTypeId());
        systemObjectAttributeClass.setClassDefaultValue(systemObjectAttributeClassRequestDto.getClassDefaultValue());
        return systemObjectAttributeClass;
    }

//    public static SystemObjectAttributeClass mapToSystemObjectAttributeClass(
//            SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
//        SystemObjectAttributeClass systemObjectAttributeClass = new SystemObjectAttributeClass();
//        systemObjectAttributeClass.setId(new SystemObjectAttributeClassId());
//        systemObjectAttributeClass.getId().setAttributeClassId(systemObjectAttributeClassRequestDto.getAttributeClassId());
//        systemObjectAttributeClass.getId().setSystemObjectTypeId(systemObjectAttributeClassRequestDto.getSystemObjectTypeId());
//        systemObjectAttributeClass.setRequired(systemObjectAttributeClass.isRequired());
//        systemObjectAttributeClass.setAttributeClass(new AttributeClass());
//        systemObjectAttributeClass.getAttributeClass().setId(systemObjectAttributeClassRequestDto.getAttributeClassId());
//        systemObjectAttributeClass.setSystemObjectType(new SystemObjectType());
//        systemObjectAttributeClass.getSystemObjectType().setId(systemObjectAttributeClassRequestDto.getSystemObjectTypeId());
//        systemObjectAttributeClass.setClassDefaultValue(systemObjectAttributeClassRequestDto.getClassDefaultValue());
//        return systemObjectAttributeClass;
//    }

    public static SystemObjectAttributeClassResponseDto mapToSystemObjectAttributeClassResponseDto(
            SystemObjectAttributeClass systemObjectAttributeClass) {
        return SystemObjectAttributeClassResponseDto.builder()
                .attributeClassId(systemObjectAttributeClass.getId().getAttributeClassId())
                .systemObjectTypeId(systemObjectAttributeClass.getId().getSystemObjectTypeId())
                .required(systemObjectAttributeClass.isRequired())
                .classDefaultValue(systemObjectAttributeClass.getClassDefaultValue())
                .attributeClassName(systemObjectAttributeClass.getAttributeClass().getName())
                .systemObjectTypeName(systemObjectAttributeClass.getSystemObjectType().getName())
                .build();
    }
}
