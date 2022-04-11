package pl.wj.bgstat.attribute.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "attributes")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long objectTypeId;
    private long objectId;
    private long attributeClassId;
    private int attributeOrdinalNumber;
    private String value;

    // TODO: 11.04.2022 Add relations to every tables that Attributes table has relations
}
