package pl.wj.bgstat.gameplay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.gameplay.model.Gameplay;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameplayRepository extends JpaRepository<Gameplay, Long> {

    @Query(
            value = "SELECT gp.board_game_id AS boardGameId, bg.name AS boardGameName, " +
                    "COUNT(gp.board_game_id) AS numOfGameplays, " +
                    "AVG(gp.playtime) AS avgTimeOfGameplay " +
                    "FROM gameplays gp LEFT JOIN board_games bg ON gp.board_game_id = bg.id " +
                    "WHERE gp.start_time >= :fromDate AND gp.end_time <= :toDate " +
                    "GROUP BY gp.board_game_id, bg.name",
            nativeQuery = true)
    List<BoardGameGameplaysStatsDto> getStatsByGivenPeriod(LocalDate fromDate, LocalDate toDate);
}
