package pl.wj.bgstat.gameplay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplayStatsDto;
import pl.wj.bgstat.gameplay.model.Gameplay;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameplayRepository extends JpaRepository<Gameplay, Long> {

    List<BoardGameGameplayStatsDto> getStatsByGivenPeriod(LocalDate fromDate, LocalDate toDate);
}
