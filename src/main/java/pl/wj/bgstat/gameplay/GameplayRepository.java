package pl.wj.bgstat.gameplay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.gameplay.model.Gameplay;
import pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto;

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

    @Query(
            value = "SELECT gp.board_game_id AS boardGameId, bg.name AS boardGameName, " +
                    "COUNT(gp.board_game_id) AS numOfGameplays, " +
                    "AVG(gp.playtime) AS avgTimeOfGameplay " +
                    "FROM gameplays gp LEFT JOIN board_games bg ON gp.board_game_id = bg.id " +
                    "WHERE gp.start_time >= :fromDate AND gp.end_time <= :toDate AND gp.user_id = :id " +
                    "GROUP BY gp.board_game_id, bg.name",
            nativeQuery = true)
    List<BoardGameGameplaysStatsDto> getStatsByUserIdAndGivenPeriod(long id, LocalDate fromDate, LocalDate toDate);


    @Query("SELECT new pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto(gp.id, bg.name, gp.playtime) " +
            "FROM Gameplay gp LEFT JOIN BoardGame bg ON gp.boardGameId = bg.id " +
            "WHERE gp.userId = :id")
    Page<GameplayHeaderDto> findUserGameplayHeaders(long id, Pageable pageable);


}
