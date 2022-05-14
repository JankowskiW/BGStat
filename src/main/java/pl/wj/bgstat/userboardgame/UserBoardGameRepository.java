package pl.wj.bgstat.userboardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetails;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeader;

import java.util.Optional;

@Repository
public interface UserBoardGameRepository extends JpaRepository<UserBoardGame, Long> {
    @Query(
        value = "SELECT ubg.id, bg.name FROM user_board_games ubg " +
                "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id " +
                "/*:pageable*/",
        nativeQuery = true)
    Page<UserBoardGameHeader> findUserBoardGameHeaders(Pageable pageable);

    @Query(
            value =  "SELECT " +
                    "        ubg.id AS id, ubg.board_game_id AS boardGameId, ubg.object_type_id AS objectTypeId," +
                    "        ubg.sleeved AS sleeved, ubg.comment AS comment, ubg.purchase_date AS purchaseDate, " +
                    "        ubg.purchase_price AS purchasePrice, ubg.sale_date AS saleDate, ubg.sale_price AS salePrice," +
                    "        bg.name AS bgName, bg.recommended_age AS bgRecommendedAge, " +
                    "        bg.min_players_number AS bgMinPlayersNumber, bg.max_players_number AS bgMaxPlayersNumber," +
                    "        bg.complexity as bgComplexity, bg.playing_time AS bgPlayingTime, bgd.description AS bgDescription " +
                    "FROM user_board_games ubg " +
                    "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id " +
                    "LEFT JOIN board_game_descriptions bgd ON bgd.board_game_id = bg.id " +
                    "WHERE ubg.id = :id",
            nativeQuery = true)
    Optional<UserBoardGameDetails> getWithDetailsById(long id);

}

