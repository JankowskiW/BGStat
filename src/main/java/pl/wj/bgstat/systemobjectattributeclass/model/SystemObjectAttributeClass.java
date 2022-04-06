package pl.wj.bgstat.systemobjectattributeclass.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name="system_object_attribute_classes")
public class SystemObjectAttributeClass {
    @EmbeddedId
    private SystemObjectAttributeClassId systemObjectAttributeClassId;
    private boolean required;
    private String classDefaultValue;
}
