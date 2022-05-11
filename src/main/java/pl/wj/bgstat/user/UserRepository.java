package pl.wj.bgstat.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
