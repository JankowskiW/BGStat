package pl.wj.bgstat.domain.stats;

import org.springframework.stereotype.Repository;
import pl.wj.bgstat.domain.stats.model.StatsMapper;
import pl.wj.bgstat.domain.stats.model.dto.StatsBoardGameGameplaysDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

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

        return StatsMapper.mapToStatsBoardGameGameplaysDtoList(query.getResultList());
    }

    @Override
    public List<StatsBoardGameGameplaysDto> getStatsByGivenPeriodAndByUserId(long userId, LocalDate fromDate, LocalDate toDate) {
        Query query = entityManager.createNativeQuery(
                        "SELECT gp.board_game_id AS boardGameId, bg.name AS boardGameName, " +
                                "COUNT(gp.board_game_id) AS numOfGameplays, " +
                                "AVG(gp.playtime) AS avgTimeOfGameplay " +
                                "FROM gameplays gp LEFT JOIN board_games bg ON gp.board_game_id = bg.id " +
                                "WHERE gp.start_time >= :fromDate AND gp.end_time <= :toDate AND gp.user_id = :userId " +
                                "GROUP BY gp.board_game_id, bg.name")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("userId", userId);

        return StatsMapper.mapToStatsBoardGameGameplaysDtoList(query.getResultList());
    }

    @Override
    public List<StatsBoardGameGameplaysDto> getStatsByGivenPeriodAndByUserIdAndByBoardGameId(
            long userId, LocalDate fromDate, LocalDate toDate, long boardGameId) {
        Query query = entityManager.createNativeQuery(
                        "SELECT gp.board_game_id AS boardGameId, bg.name AS boardGameName, " +
                                "COUNT(gp.board_game_id) AS numOfGameplays, " +
                                "AVG(gp.playtime) AS avgTimeOfGameplay " +
                                "FROM gameplays gp LEFT JOIN board_games bg ON gp.board_game_id = bg.id " +
                                "WHERE gp.start_time >= :fromDate AND gp.end_time <= :toDate AND " +
                                "gp.user_id = :userId AND gp.board_game_id = :boardGameId " +
                                "GROUP BY gp.board_game_id, bg.name")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("userId", userId)
                .setParameter("boardGameId", boardGameId);

        return StatsMapper.mapToStatsBoardGameGameplaysDtoList(query.getResultList());
    }

    @Override
    public List<StatsBoardGameGameplaysDto> getStatsByGivenPeriodAndByBoardGameId(
            long boardGameId, LocalDate fromDate, LocalDate toDate) {
        Query query = entityManager.createNativeQuery(
                        "SELECT gp.board_game_id AS boardGameId, bg.name AS boardGameName, " +
                                "COUNT(gp.board_game_id) AS numOfGameplays, " +
                                "AVG(gp.playtime) AS avgTimeOfGameplay " +
                                "FROM gameplays gp LEFT JOIN board_games bg ON gp.board_game_id = bg.id " +
                                "WHERE gp.start_time >= :fromDate AND gp.end_time <= :toDate AND " +
                                "gp.board_game_id = :boardGameId " +
                                "GROUP BY gp.board_game_id, bg.name")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("boardGameId", boardGameId);

        return StatsMapper.mapToStatsBoardGameGameplaysDtoList(query.getResultList());
    }
}
