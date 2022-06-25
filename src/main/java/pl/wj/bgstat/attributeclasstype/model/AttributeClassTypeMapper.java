package pl.wj.bgstat.attributeclasstype.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttributeClassTypeMapper {

    public static AttributeClassType mapToAttributeClassType(long id, AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        AttributeClassType attributeClassType = mapToAttributeClassType(attributeClassTypeRequestDto);
        attributeClassType.setId(id);
        return attributeClassType;
    }

    public static AttributeClassType mapToAttributeClassType(AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        AttributeClassType attributeClassType = new AttributeClassType();
        attributeClassType.setName(attributeClassTypeRequestDto.getName());
        attributeClassType.setDescription(attributeClassTypeRequestDto.getDescription());
        attributeClassType.setArchived(attributeClassTypeRequestDto.isArchived());
        attributeClassType.setMultivalued(attributeClassType.isMultivalued());
        return attributeClassType;
    }
}
