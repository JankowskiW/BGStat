package pl.wj.bgstat.domain.user.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.security.role.model.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;

    /* TODO: 12.07.2022 Try to change schema-validation naming convention from "users_roles" to "user_roles" and "roles_id" field name to "role_id"
        or even create user_roles module and change @ManyToMany relation to @OneToMany user->user_roles and @OneToMany role->user_roles relations
    */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private List<Role> roles = new ArrayList<>();
}
