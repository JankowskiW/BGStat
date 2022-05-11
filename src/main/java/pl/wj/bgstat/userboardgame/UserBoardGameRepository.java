package pl.wj.bgstat.userboardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;

import java.util.Optional;

@Repository
public interface UserBoardGameRepository extends JpaRepository<UserBoardGame, Long> {
    // TODO: 08.05.2022 Check if Pageable works with native queries like in example below 
    @Query(
        value = "SELECT ubg.id, bg.name FROM user_board_games ubg" +
                "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id" +
                "/*:pageable*/",
        nativeQuery = true)
    Page<UserBoardGameHeaderDto> findUserBoardGameHeaders(Pageable pageable);

    @Query(
            value = "SELECT new pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto(" +
                    "ubg.id, ubg.board_game_id, ubg.object_type_id, ubg.sleeved, ubg.comment," +
                    "        ubg.purchase_date, ubg.purchase_price, ubg.sale_date, ubg.sale_price," +
                    "        bg.name, bg.recommended_age, bg.min_players_number, bg.max_players_number," +
                    "        bg.complexity, bg.playing_time, bgd.description)" +
                    "FROM user_board_games ubg " +
                    "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id" +
                    "LEFT JOIN board_game_descriptions ON bgd.board_game_id = bg.id" +
                    "WHERE ubg.id = :id",
            nativeQuery = true
    )
    Optional<UserBoardGameDetailsDto> findWithDetailsById(long id);
}

