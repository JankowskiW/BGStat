package pl.wj.bgstat.attributeclasstype.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pl.wj.bgstat.attributeclass.model.AttributeClass;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="attribute_class_types")
public class AttributeClassType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @ColumnDefault("false")
    private boolean archived;
}
