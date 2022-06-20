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

//    @Query("SELECT new pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto(gp.id, bg.name, gp.playtime) " +
//            "FROM Gameplay gp LEFT JOIN BoardGame bg ON gp.boardGameId = bg.id " +
//            "WHERE gp.userId = :id")
    @Query("SELECT new pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto(gp.id, bg.name, gp.playtime) " +
            "FROM Gameplay gp LEFT JOIN BoardGame bg ON gp.boardGame.id = bg.id " +
            "WHERE gp.userId = :id")
    Page<GameplayHeaderDto> findUserGameplayHeaders(long id, Pageable pageable);


}
