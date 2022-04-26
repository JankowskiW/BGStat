package pl.wj.bgstat.attribute.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttributeMapper {

    public static Attribute mapToAttribute(long id, AttributeRequestDto attributeRequestDto) {
        Attribute attribute = mapToAttribute(attributeRequestDto);
        attribute.setId(id);
        return attribute;
    }

    public static Attribute mapToAttribute(AttributeRequestDto attributeRequestDto) {
        Attribute attribute = new Attribute();
        attribute.setObjectTypeId(attributeRequestDto.getObjectTypeId());
        attribute.setObjectId(attributeRequestDto.getObjectId());
        attribute.setAttributeClassId(attributeRequestDto.getAttributeClassId());
        attribute.setValue(attributeRequestDto.getValue());
        return attribute;
    }

    public static AttributeResponseDto mapToAttributeResponseDto(Attribute attribute) {
        return AttributeResponseDto.builder()
                .objectTypeId(attribute.getObjectTypeId())
                .objectId(attribute.getObjectId())
                .attributeClassId(attribute.getAttributeClassId())
                .value(attribute.getValue())
                .id(attribute.getId())
                .build();
    }

    public static AttributeResponseDto mapToAttributeResponseDto(AttributeRequestDto attributeRequestDto, long id) {
        return AttributeResponseDto.builder()
                .objectTypeId(attributeRequestDto.getObjectTypeId())
                .objectId(attributeRequestDto.getObjectId())
                .attributeClassId(attributeRequestDto.getAttributeClassId())
                .value(attributeRequestDto.getValue())
                .id(id)
                .build();
    }

}
