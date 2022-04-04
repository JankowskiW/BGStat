package pl.wj.bgstat.attributeclass.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="attribute_classes")
public class AttributeClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long attributeClassTypeId;
    private String name;
    private String description;
}
