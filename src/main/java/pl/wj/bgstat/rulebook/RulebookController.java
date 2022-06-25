package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    public RulebookResponseDto addRulebook(@RequestPart @Valid RulebookRequestDto rulebookRequestDto,
                                           @RequestPart("rulebook") MultipartFile rulebook) {
        return rulebookService.addRulebook(rulebookRequestDto, rulebook);
    }

    @PutMapping("/{id}")
    public RulebookResponseDto editRulebook(@PathVariable long id, @RequestBody MultipartFile rulebook) {
        return rulebookService.editRulebook(id, rulebook);
    }

    @DeleteMapping("/{id}")
    public void deleteRulebook(@PathVariable long id) {
        rulebookService.deleteRulebook(id);
    }
}
