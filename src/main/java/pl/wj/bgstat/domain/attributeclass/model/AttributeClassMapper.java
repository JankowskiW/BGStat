package pl.wj.bgstat.domain.attributeclass.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.domain.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.domain.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.domain.attributeclasstype.model.AttributeClassType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttributeClassMapper {

    public static AttributeClass mapToAttributeClass(long id, AttributeClassRequestDto attributeClassRequestDto) {
        AttributeClass attributeClass = mapToAttributeClass(attributeClassRequestDto);
        attributeClass.setId(id);
        return  attributeClass;
    }

    public static AttributeClass mapToAttributeClass(AttributeClassRequestDto attributeClassRequestDto) {
        AttributeClass attributeClass = new AttributeClass();
        attributeClass.setName(attributeClassRequestDto.getName());
        attributeClass.setDescription(attributeClassRequestDto.getDescription());
        attributeClass.setAttributeClassType(new AttributeClassType());
        attributeClass.getAttributeClassType().setId(attributeClassRequestDto.getAttributeClassTypeId());
        return attributeClass;
    }

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
