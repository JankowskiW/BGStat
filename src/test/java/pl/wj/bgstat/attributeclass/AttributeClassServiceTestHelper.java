package pl.wj.bgstat.attributeclass;

import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;

import java.util.ArrayList;
import java.util.List;

public class AttributeClassServiceTestHelper {

    public static List<AttributeClass> populateAttributeClassList(int numberOfElements) {
        List<AttributeClass> attributeClassList = new ArrayList<>();
        AttributeClass attributeClass;
        for (int i = 0; i <= numberOfElements; i++) {
            attributeClass = new AttributeClass();
            attributeClass.setId(i);
            attributeClass.setAttributeClassTypeId(i%6);
            attributeClass.setName("Name No. " + i);
            attributeClass.setDescription("DESCRIPTION OF " + attributeClass.getName());
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
                1,
                "Name No. " + (currentSize + 1),
                "DESCRIPTION OF Name No. " + (currentSize + 1));
    }
}
