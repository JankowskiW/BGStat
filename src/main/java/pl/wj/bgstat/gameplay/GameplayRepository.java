package pl.wj.bgstat.gameplay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.gameplay.model.Gameplay;

import java.time.LocalDate;

@Repository
public interface GameplayRepository extends JpaRepository<Gameplay, Long> {
    long countInGivenPeriod(LocalDate fromDate, LocalDate toDate);
}
