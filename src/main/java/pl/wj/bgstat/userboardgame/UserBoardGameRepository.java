package pl.wj.bgstat.userboardgame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.userboardgame.model.UserBoardGame;

@Repository
public interface UserBoardGameRepository extends JpaRepository<UserBoardGame, Long> {
}
