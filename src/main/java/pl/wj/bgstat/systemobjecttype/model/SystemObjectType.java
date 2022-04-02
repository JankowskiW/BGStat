package pl.wj.bgstat.systemobjecttype.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="system_object_types")
public class SystemObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @ColumnDefault("true")
    private boolean archivized;
}
