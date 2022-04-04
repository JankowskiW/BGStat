package pl.wj.bgstat.attributeclass.model;


import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;

import javax.persistence.*;

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
}
