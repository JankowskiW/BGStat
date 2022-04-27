package pl.wj.bgstat.attributeclasstype;

import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

import java.util.ArrayList;
import java.util.List;

public class AttributeClassTypeServiceTestHelper {

    public static List<AttributeClassType> populateAttributeClassTypeList(int numberOfElements) {
        List<AttributeClassType> attributeClassTypeList = new ArrayList<>();
        AttributeClassType attributeClassType;
        for (int i = 1; i <= numberOfElements; i++) {
            attributeClassType = new AttributeClassType();
            attributeClassType.setId(i);
            attributeClassType.setName("Name No. " + i);
            attributeClassType.setDescription("DESCRIPTION OF " + attributeClassType.getName());
            attributeClassType.setArchived(i%2==0);
            attributeClassTypeList.add(attributeClassType);
        }
        return attributeClassTypeList;
    }

    public static List<AttributeClassTypeHeaderDto> populateAttributeClassTypeHeaderDtoList(List<AttributeClassType> attributeClassTypeList) {
        List<AttributeClassTypeHeaderDto> attributeClassTypeHeaderDtoList = new ArrayList<>();
        for(AttributeClassType attributeClassType : attributeClassTypeList) {
            attributeClassTypeHeaderDtoList.add(new AttributeClassTypeHeaderDto(
                    attributeClassType.getId(), attributeClassType.getName(), attributeClassType.isArchived()));
        }
        return attributeClassTypeHeaderDtoList;
    }

    public static AttributeClassTypeRequestDto createAttributeClassTypeRequestDto(int currentSize) {
        return new AttributeClassTypeRequestDto(
                "Name No. " + (currentSize + 1),
                "DESCRIPTION OF Name No. " + (currentSize + 1),
                false);
    }
}
