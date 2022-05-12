package pl.wj.bgstat.systemobjecttype.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="system_object_types", schema="dbo")
public class SystemObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @ColumnDefault("false")
    private boolean archived;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "systemObjectType")
    private Set<SystemObjectAttributeClass> attributeClasses;


}
