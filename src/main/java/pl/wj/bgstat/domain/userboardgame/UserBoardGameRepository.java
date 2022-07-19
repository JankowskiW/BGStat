package pl.wj.bgstat.domain.userboardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.wj.bgstat.domain.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.util.Optional;

@Repository
public interface UserBoardGameRepository extends JpaRepository<UserBoardGame, Long> {
    @Query(
        value = "SELECT ubg.id AS id, bg.name AS bgName FROM user_board_games ubg " +
                "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id " +
                "WHERE ubg.user_id = :userId " +
                "ORDER BY ubg.id",
        countQuery = "SELECT count(*) FROM user_board_games ubg WHERE ubg.user_id = :userId",
        nativeQuery = true)
    Page<UserBoardGameHeaderDto> findUserBoardGameHeaders(long userId, Pageable pageable);

    @Query(
            value =  "SELECT " +
                    "        ubg.id AS id, ubg.board_game_id AS boardGameId, ubg.object_type_id AS objectTypeId," +
                    "        ubg.sleeved AS sleeved, ubg.comment AS comment, ubg.purchase_date AS purchaseDate, " +
                    "        ubg.purchase_price AS purchasePrice, ubg.sale_date AS saleDate, ubg.sale_price AS salePrice," +
                    "        bg.name AS bgName, bg.recommended_age AS bgRecommendedAge, " +
                    "        bg.min_players_number AS bgMinPlayersNumber, bg.max_players_number AS bgMaxPlayersNumber," +
                    "        bg.complexity as bgComplexity, bg.estimated_playtime AS bgEstimatedPlaytime, bgd.description AS bgDescription " +
                    "FROM user_board_games ubg " +
                    "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id " +
                    "LEFT JOIN board_game_descriptions bgd ON bgd.board_game_id = bg.id " +
                    "WHERE ubg.id = :id",
            nativeQuery = true)
    Optional<UserBoardGameDetailsDto> getWithDetailsById(long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserBoardGame ubg WHERE ubg.boardGameId = :id")
    void deleteByBoardGameId(long id);
}

