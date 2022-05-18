package pl.wj.bgstat.gameplay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GameplayService {

    private final GameplayRepository gameplayRepository;

    public long getGameplayActivity(LocalDate fromDate, LocalDate toDate) {
        return 0;
    }
}
