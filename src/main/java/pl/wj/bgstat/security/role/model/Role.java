package pl.wj.bgstat.security.role.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.security.privilege.model.Privilege;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Privilege> privileges = new ArrayList<>();
}
