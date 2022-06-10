package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rulebooks")
public class RulebookController {
    private final RulebookService rulebookService;
}
