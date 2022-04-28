package pl.wj.bgstat.attribute;

import pl.wj.bgstat.attribute.model.Attribute;

import java.util.ArrayList;
import java.util.List;

public class AttributeServiceTestHelper {

    private static final int NUMBER_OF_ELEMENTS = 20;

    public static List<Attribute> populateAttributeList() {
        List<Attribute> attributeList = new ArrayList<>();
        Attribute attribute;

        // Assuming that attributeClassId == 3 is an attribute class of multivalued type
        for (int i = 1; i <= NUMBER_OF_ELEMENTS; i++) {
            attribute = new Attribute();
            attribute.setId(i);
            attribute.setValue("VAL NO. " + i);

            if (i <= 5) {
                attribute.setObjectTypeId(1);
                attribute.setObjectId(i);
                attribute.setAttributeClassId(1);
            } else if (i <= 10) {
                attribute.setObjectTypeId(2);
                attribute.setObjectId(i);
                attribute.setAttributeClassId(1);
            } else if (i <= 15) {
                attribute.setObjectTypeId(1);
                attribute.setObjectId(i);
                attribute.setAttributeClassId(2);
            } else {
                attribute.setObjectTypeId(1);
                attribute.setObjectId(NUMBER_OF_ELEMENTS);
                attribute.setAttributeClassId(3);
            }
            attributeList.add(attribute);
        }
        return attributeList;
    }
}
