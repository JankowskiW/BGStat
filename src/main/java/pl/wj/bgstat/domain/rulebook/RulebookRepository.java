package pl.wj.bgstat.domain.rulebook;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.domain.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.domain.rulebook.model.Rulebook;

public interface RulebookRepository extends JpaRepository<Rulebook, Long> {
    boolean existsByBoardGameIdAndLanguageIso(long boardGameId, LanguageISO languageIso);

    void deleteByBoardGameId(long boardGameId);
}
