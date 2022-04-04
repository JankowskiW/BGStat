package pl.wj.bgstat.attributeclass.model;

import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;

public class AttributeClassMapper {
    public static AttributeClassResponseDto mapToAttributeClassResponseDto(AttributeClass attributeClass) {
        return AttributeClassResponseDto.builder()
                .id(attributeClass.getId())
                .name(attributeClass.getName())
                .description(attributeClass.getDescription())
                .attributeClassTypeId(attributeClass.getAttributeClassType().getId())
                .attributeClassTypeName(attributeClass.getAttributeClassType().getName())
                .build();
    }
}
