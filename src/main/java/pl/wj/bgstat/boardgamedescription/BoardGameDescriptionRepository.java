package pl.wj.bgstat.boardgamedescription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;

@Repository
public interface BoardGameDescriptionRepository extends JpaRepository<BoardGameDescription, Long> {
}
