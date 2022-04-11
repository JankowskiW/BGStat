package pl.wj.bgstat.attribute.model;

import pl.wj.bgstat.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;

public class AttributeMapper {

    public Attribute mapToAttribute(AttributeRequestDto attributeRequestDto) {
        Attribute attribute = new Attribute();
        attribute.setObjectTypeId(attributeRequestDto.getObjectTypeId());
        attribute.setObjectId(attributeRequestDto.getObjectId());
        attribute.setAttributeClassId(attributeRequestDto.getAttributeClassId());
        attribute.setValue(attributeRequestDto.getValue());
        return attribute;
    }

    public AttributeResponseDto mapToAttributeResponseDto(Attribute attribute) {
        return AttributeResponseDto.builder()
                .objectTypeId(attribute.getObjectTypeId())
                .objectId(attribute.getObjectId())
                .attributeClassId(attribute.getAttributeClassId())
                .attributeOrdinalNumber(attribute.getAttributeOrdinalNumber())
                .value(attribute.getValue())
                .build();
    }
}
