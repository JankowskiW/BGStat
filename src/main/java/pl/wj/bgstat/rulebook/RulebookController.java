package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rulebooks")
public class RulebookController {
    private final RulebookService rulebookService;

    @PostMapping("")
    public RulebookResponseDto addOrReplaceRulebook(@RequestPart @Valid RulebookRequestDto rulebookRequestDto,
                                                    @RequestPart("rulebook") MultipartFile rulebook) {
        return rulebookService.addOrReplaceRulebook(rulebookRequestDto, rulebook);
    }
}
