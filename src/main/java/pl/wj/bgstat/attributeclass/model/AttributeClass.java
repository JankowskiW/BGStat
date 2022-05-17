package pl.wj.bgstat.attributeclass.model;


import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="attribute_classes")
public class AttributeClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private AttributeClassType attributeClassType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributeClass")
    private Set<SystemObjectAttributeClass> systemObjectTypes;
}
