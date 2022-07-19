package pl.wj.bgstat.domain.attributeclasstype.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wj.bgstat.domain.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

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
        attributeClassType.setMultivalued(attributeClassTypeRequestDto.isMultivalued());
        return attributeClassType;
    }
}
