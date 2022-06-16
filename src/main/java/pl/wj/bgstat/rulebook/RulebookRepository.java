package pl.wj.bgstat.rulebook;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.rulebook.model.Rulebook;

public interface RulebookRepository extends JpaRepository<Rulebook, Long> {
    boolean existsByBoardGameIdAndLanguageIso(long boardGameId, LanguageISO languageIso);

    void deleteByBoardGameId(long boardGameId);
}
