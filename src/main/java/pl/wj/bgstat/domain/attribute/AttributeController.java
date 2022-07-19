package pl.wj.bgstat.domain.attribute;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.domain.attribute.model.dto.AttributeResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attributes")
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping("/{id}")
    public AttributeResponseDto getSingleAttribute(@PathVariable long id) {
        return attributeService.getSingleAttribute(id);
    }

    @PostMapping("")
    public AttributeResponseDto addAttribute(@RequestBody @Valid AttributeRequestDto attributeRequestDto) {
        return attributeService.addAttribute(attributeRequestDto);
    }

    @PutMapping("/{id}")
    public AttributeResponseDto editAttribute(
            @PathVariable long id, @RequestBody @Valid AttributeRequestDto attributeRequestDto) {
        return attributeService.editAttribute(id, attributeRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAttribute(@PathVariable long id) {
        attributeService.deleteAttribute(id);
    }
}
