package pl.wj.bgstat.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> getByUsername(String username);
}
