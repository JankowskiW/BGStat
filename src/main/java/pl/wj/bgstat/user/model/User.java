package pl.wj.bgstat.user.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.role.model.Role;

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
    @ManyToMany(fetch = FetchType.EAGER)
    /* TODO: 12.07.2022 Try to change schema-validation naming convention from "users_roles" to "user_roles" and "roles_id" field name to "role_id"
        or even create user_roles module and change @ManyToMany relation to @OneToMany user->user_roles and @OneToMany role->user_roles relations
    */
    private List<Role> roles = new ArrayList<>();
}
