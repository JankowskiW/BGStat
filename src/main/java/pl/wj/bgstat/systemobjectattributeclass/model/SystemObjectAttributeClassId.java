package pl.wj.bgstat.systemobjectattributeclass.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class SystemObjectAttributeClassId implements Serializable {
    private long attributeClassId;
    private long systemObjectTypeId;
}
