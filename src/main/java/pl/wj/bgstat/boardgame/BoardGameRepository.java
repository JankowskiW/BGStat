package pl.wj.bgstat.boardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    @Query("SELECT new pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto(bg.id, bg.name) FROM BoardGame bg")
    Page<BoardGameHeaderDto> findBoardGameHeaders(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg LEFT JOIN FETCH bg.boardGameDescription bgd WHERE bg.id = :id")
    Optional<BoardGame> findWithDescriptionById(long id);

    boolean existsByNameAndIdNot(String name, long id);
    boolean existsByName(String name);

    @Query("SELECT bg FROM BoardGame bg")
    BoardGameGameplaysStatsDto getStatsByGivenPeriod(long id, LocalDate fromDate, LocalDate toDate);
}
