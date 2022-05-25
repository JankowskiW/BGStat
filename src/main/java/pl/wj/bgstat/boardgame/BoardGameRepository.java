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
    @Query(
            value = "SELECT bg.id AS boardGameId, bg.name AS boardGameName, " +
                    "ISNULL(gp.numOfGameplays, 0) AS numOfGameplays, ISNULL(gp.avgTimeOfGameplay, 0) AS avgTimeOfGameplay " +
                    "FROM board_games bg LEFT JOIN (SELECT board_game_id, COUNT(board_game_id) AS numOfGameplays, " +
                    "AVG(playtime) AS avgTimeOfGameplay FROM gameplays WHERE board_game_id = :id AND " +
                    "start_time >= :fromDate AND end_time <= :toDate GROUP BY board_game_id) AS gp ON bg.id = gp.board_game_id " +
                    "WHERE bg.id = :id" ,
            nativeQuery = true)
    BoardGameGameplaysStatsDto getStatsByGivenPeriod(long id, LocalDate fromDate, LocalDate toDate);

}
