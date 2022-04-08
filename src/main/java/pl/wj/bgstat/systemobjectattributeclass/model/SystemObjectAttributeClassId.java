package pl.wj.bgstat.systemobjectattributeclass.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SystemObjectAttributeClassId implements Serializable {
    private long attributeClassId;
    private long systemObjectTypeId;
}
