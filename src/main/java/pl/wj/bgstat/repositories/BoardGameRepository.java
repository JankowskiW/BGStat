package pl.wj.bgstat.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.models.BoardGame;

import java.util.List;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    @Query("SELECT bg FROM BoardGame bg")
    List<BoardGame> findAllBoardGames(Pageable page);

    boolean existsByName(String name);
}
