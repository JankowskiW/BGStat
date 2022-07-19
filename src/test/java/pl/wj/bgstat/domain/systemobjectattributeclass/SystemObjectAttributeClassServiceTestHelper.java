package pl.wj.bgstat.domain.systemobjectattributeclass;

import pl.wj.bgstat.domain.attributeclass.model.AttributeClass;
import pl.wj.bgstat.domain.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.SystemObjectAttributeClassId;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.domain.systemobjecttype.model.SystemObjectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SystemObjectAttributeClassServiceTestHelper {

    public static List<SystemObjectType> populateSystemObjectTypeList(int numberOfElements) {
        List<SystemObjectType> systemObjectTypeList = new ArrayList<>();
        SystemObjectType systemObjectType;
        for (int i = 1; i <= numberOfElements; i++) {
            systemObjectType = new SystemObjectType();
            systemObjectType.setId(i);
            systemObjectType.setName("Name No. " + i);
            systemObjectType.setArchived(false);
            systemObjectType.setDescription("DESCRIPTION " + i);
            systemObjectType.setAttributeClasses(new HashSet<>());
            systemObjectTypeList.add(systemObjectType);
        }
        return systemObjectTypeList;
    }

    public static List<AttributeClass> populateAttributeClassList(int numberOfElements) {
        List<AttributeClass> attributeClassList = new ArrayList<>();
        AttributeClass attributeClass;
        for (int i = 1; i <= numberOfElements; i++) {
            attributeClass = new AttributeClass();
            attributeClass.setId(i);
            attributeClass.setName("Class Name No. " + i);
            attributeClass.setDescription("DESCRIPTION " + i);
            attributeClass.setAttributeClassType(new AttributeClassType());
            attributeClass.setSystemObjectTypes(new HashSet<>());
            attributeClassList.add(attributeClass);
        }
        return attributeClassList;
    }

    public static List<SystemObjectAttributeClass> populateSystemObjectAttributeClassList(
                         List<AttributeClass> attributeClassList, List<SystemObjectType> systemObjectTypeList) {
        List<SystemObjectAttributeClass> systemObjectAttributeClassList = new ArrayList<>();
        SystemObjectAttributeClass systemObjectAttributeClass;
        for (int i = 0; i < attributeClassList.size(); i++) {
            systemObjectAttributeClass = createSystemObjectAttributeClass(
                    attributeClassList.get(i), systemObjectTypeList.get(i));
            systemObjectAttributeClassList.add(systemObjectAttributeClass);
            attributeClassList.get(i).getSystemObjectTypes().add(systemObjectAttributeClass);
            systemObjectTypeList.get(i).getAttributeClasses().add(systemObjectAttributeClass);

            systemObjectAttributeClass = createSystemObjectAttributeClass(
                    attributeClassList.get(i), systemObjectTypeList.get(i+1));
            systemObjectAttributeClassList.add(systemObjectAttributeClass);
            attributeClassList.get(i).getSystemObjectTypes().add(systemObjectAttributeClass);
            systemObjectTypeList.get(i+1).getAttributeClasses().add(systemObjectAttributeClass);
        }
        return systemObjectAttributeClassList;
    }

    public static SystemObjectAttributeClassRequestDto createSystemObjectAttributeClassRequestDto(
            SystemObjectAttributeClassId id) {
        return new SystemObjectAttributeClassRequestDto(
                id.getAttributeClassId(), id.getSystemObjectTypeId(), false, "DEFAULT");
    }

    private static SystemObjectAttributeClass createSystemObjectAttributeClass(
            AttributeClass attributeClass, SystemObjectType systemObjectType
    ) {
        SystemObjectAttributeClass systemObjectAttributeClass = new SystemObjectAttributeClass();
        systemObjectAttributeClass.setId(new SystemObjectAttributeClassId());
        systemObjectAttributeClass.getId().setAttributeClassId(attributeClass.getId());
        systemObjectAttributeClass.getId().setSystemObjectTypeId(systemObjectType.getId());
        systemObjectAttributeClass.setAttributeClass(attributeClass);
        systemObjectAttributeClass.setSystemObjectType(systemObjectType);
        systemObjectAttributeClass.setRequired(false);
        systemObjectAttributeClass.setClassDefaultValue("DEFAULT");
        return systemObjectAttributeClass;
    }
}
