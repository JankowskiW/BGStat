package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RulebookService {
    private final RulebookRepository rulebookRepository;
}
