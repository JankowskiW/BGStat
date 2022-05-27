package pl.wj.bgstat.stats;

import org.springframework.stereotype.Repository;
import pl.wj.bgstat.stats.model.StatsMapper;
import pl.wj.bgstat.stats.model.dto.StatsBoardGameGameplaysDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

import static pl.wj.bgstat.stats.model.StatsMapper.mapToStatsBoardGameGameplaysDtoList;

@Repository
public class StatsRepository implements CustomStatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StatsBoardGameGameplaysDto> getStatsByGivenPeriod(LocalDate fromDate, LocalDate toDate) {
        Query query = entityManager.createNativeQuery(
                "SELECT gp.board_game_id AS boardGameId, bg.name AS boardGameName, " +
                    "COUNT(gp.board_game_id) AS numOfGameplays, " +
                    "AVG(gp.playtime) AS avgTimeOfGameplay " +
                    "FROM gameplays gp LEFT JOIN board_games bg ON gp.board_game_id = bg.id " +
                    "WHERE gp.start_time >= :fromDate AND gp.end_time <= :toDate " +
                    "GROUP BY gp.board_game_id, bg.name")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate);

        return mapToStatsBoardGameGameplaysDtoList(query.getResultList());
    }

}
