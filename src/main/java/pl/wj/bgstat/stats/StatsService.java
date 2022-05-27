package pl.wj.bgstat.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.stats.model.dto.StatsGameplaysResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public StatsGameplaysResponseDto getGameplaysStats(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
}
