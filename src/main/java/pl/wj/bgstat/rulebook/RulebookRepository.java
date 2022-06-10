package pl.wj.bgstat.rulebook;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.rulebook.model.Rulebook;

public interface RulebookRepository extends JpaRepository<Rulebook, Long> {
}
