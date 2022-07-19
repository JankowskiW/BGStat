package pl.wj.bgstat.domain.gameplay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.wj.bgstat.domain.gameplay.model.Gameplay;
import pl.wj.bgstat.domain.gameplay.model.dto.GameplayHeaderDto;

@Repository
public interface GameplayRepository extends JpaRepository<Gameplay, Long> {

    @Query("SELECT new pl.wj.bgstat.domain.gameplay.model.dto.GameplayHeaderDto(gp.id, bg.name, gp.playtime) " +
            "FROM Gameplay gp LEFT JOIN BoardGame bg ON gp.boardGameId = bg.id " +
            "WHERE gp.userId = :id")
    Page<GameplayHeaderDto> findUserGameplayHeaders(long id, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM Gameplay gp WHERE gp.boardGameId = :id")
    void deleteByBoardGameId(long id);
}
