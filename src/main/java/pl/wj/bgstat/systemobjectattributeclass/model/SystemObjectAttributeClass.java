package pl.wj.bgstat.systemobjectattributeclass.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="system_object_attribute_classes")
public class SystemObjectAttributeClass {
    @EmbeddedId
    private SystemObjectAttributeClassId id;
    private boolean required;
    private String classDefaultValue;

    @ManyToOne
    @MapsId("attributeClassId")
    @JoinColumn(name = "attributeClassId")
    private AttributeClass attributeClass;
    @ManyToOne
    @MapsId("systemObjectTypeId")
    @JoinColumn(name = "systemObjectTypeId")
    private SystemObjectType systemObjectType;
}
