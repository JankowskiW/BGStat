package pl.wj.bgstat.attribute;

import pl.wj.bgstat.attribute.model.Attribute;

import java.util.ArrayList;
import java.util.List;

public class AttributeServiceTestHelper {

    private static final int NUMBER_OF_OBJECTS = 5;
    private static final int NUMBER_OF_OBJECT_TYPES = 2;
    private static final int NUMBER_OF_ATTRIBUTE_CLASSES = 2;

    public static List<Attribute> populateAttributeList(int numberOfElements) {
        List<Attribute> attributeList = new ArrayList<>();
        Attribute attribute;
        for (int i = 1; i <= numberOfElements; i++) {
            attribute = new Attribute();
            attribute.setId(i);
            attribute.setValue("VAL NO. " + i);
            if (i <= numberOfElements/4) {
                attribute.setAttributeOrdinalNumber(i);
                attribute.setObjectId(1);
                attribute.setObjectTypeId(1);
                attribute.setAttributeClassId(1);
            } else {
                attribute.setAttributeOrdinalNumber(1);
                attribute.setObjectId(i-(numberOfElements/4));
                attribute.setObjectTypeId(i-(numberOfElements/4));
                attribute.setAttributeClassId(i-(numberOfElements/4));
            }
            attributeList.add(attribute);
        }
        return attributeList;
    }
}
