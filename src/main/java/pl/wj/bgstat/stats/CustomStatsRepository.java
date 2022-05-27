package pl.wj.bgstat.stats;

import pl.wj.bgstat.stats.model.dto.StatsBoardGameGameplaysDto;

import java.time.LocalDate;
import java.util.List;

interface CustomStatsRepository {
    List<StatsBoardGameGameplaysDto> getStatsByGivenPeriod(LocalDate fromDate, LocalDate toDate);
}
