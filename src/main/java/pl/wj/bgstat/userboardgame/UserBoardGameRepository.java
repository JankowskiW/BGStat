package pl.wj.bgstat.userboardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;

import javax.transaction.Transactional;
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

    @Query(nativeQuery = true)
    Optional<UserBoardGameDetailsDto> getWithDetailsById(long id);
}

