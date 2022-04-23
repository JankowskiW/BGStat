package pl.wj.bgstat.attributeclass;

import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttributeClassServiceTestHelper {

    private static final int NUMBER_OF_TYPES = 6;

    public static List<AttributeClass> populateAttributeClassList(int numberOfElements) {
        List<AttributeClass> attributeClassList = new ArrayList<>();
        AttributeClass attributeClass;
        AttributeClassType attributeClassType;
        for (int i = 0; i <= numberOfElements; i++) {
            attributeClass = new AttributeClass();
            attributeClass.setId(i);
            attributeClass.setName("Name No. " + i);
            attributeClass.setDescription("DESCRIPTION OF " + attributeClass.getName());
            attributeClassType = new AttributeClassType();
            attributeClassType.setId(i % NUMBER_OF_TYPES);
            attributeClassType.setName("Type Name No. " + i % NUMBER_OF_TYPES);
            attributeClassType.setDescription("Type Description of type No. " + i % NUMBER_OF_TYPES);
            attributeClassType.setArchived(false);
            attributeClass.setAttributeClassType(attributeClassType);
            attributeClassList.add(attributeClass);
        }
        return attributeClassList;
    }

    public static List<AttributeClassHeaderDto> populateAttributeClassHeaderDtoList(List<AttributeClass> attributeClassList) {
        List<AttributeClassHeaderDto> attributeClassHeaderDtoList = new ArrayList<>();
        for(AttributeClass attributeClass : attributeClassList) {
            attributeClassHeaderDtoList.add(new AttributeClassHeaderDto(
                    attributeClass.getId(), attributeClass.getName()
            ));
        }
        return attributeClassHeaderDtoList;
    }

    public static AttributeClassRequestDto createAttributeClassRequestDto(int currentSize) {
        return new AttributeClassRequestDto(
                "Name No. " + (currentSize + 1),
                "DESCRIPTION OF Name No. " + (currentSize + 1),
                1);
    }

    public static List<SystemObjectAttributeClassResponseDto> populateSystemObjectAttributeClassResponseDtoList(int numberOfElements) {
        List<SystemObjectAttributeClassResponseDto> systemObjectAttributeClassResponseDtoList = new ArrayList<>();
        for (int i = 1; i < numberOfElements; i++) {
            systemObjectAttributeClassResponseDtoList.add(createSystemObjectAttributeClassResponseDto(i, i));
            systemObjectAttributeClassResponseDtoList.add(createSystemObjectAttributeClassResponseDto(i, i+1));
        }
        return systemObjectAttributeClassResponseDtoList;
    }

    private static SystemObjectAttributeClassResponseDto createSystemObjectAttributeClassResponseDto(
                                            long attributeClassId, long systemObjectTypeId) {
        return SystemObjectAttributeClassResponseDto.builder()
                .attributeClassId(attributeClassId)
                .systemObjectTypeId(systemObjectTypeId)
                .required(false)
                .classDefaultValue("DEFAULT VAL " + attributeClassId)
                .attributeClassName("CLASS NAME " + attributeClassId)
                .systemObjectTypeName("OBJECT TYPE NAME " + systemObjectTypeId)
                .build();
    }
}
