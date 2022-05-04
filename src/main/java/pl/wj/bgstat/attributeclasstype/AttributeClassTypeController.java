package pl.wj.bgstat.attributeclasstype;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeArchivedStatus;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute-class-types")
public class AttributeClassTypeController {

    private final AttributeClassTypeService attributeClassTypeService;

    @GetMapping("")
    public List<AttributeClassTypeHeaderDto> getAttributeClassTypeHeaders(
            @RequestParam(required = false, defaultValue = "ACTIVE") AttributeClassTypeArchivedStatus archivedStatus) {
        return attributeClassTypeService.getAttributeClassTypeHeaders(archivedStatus);
    }

    @GetMapping("/{id}")
    public AttributeClassType getSingleAttributeClassType(@PathVariable long id) {
        return attributeClassTypeService.getSingleAttributeClassType(id);
    }

    @PostMapping("")
    public AttributeClassType addAttributeClassType(@RequestBody @Valid AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        return attributeClassTypeService.addAttributeClassType(attributeClassTypeRequestDto);
    }

    @PutMapping("/{id}")
    public AttributeClassType editAttributeClassType(@PathVariable long id,
                                 @RequestBody @Valid AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        return attributeClassTypeService.editAttributeClassType(id, attributeClassTypeRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAttributeClassType(@PathVariable long id) {
        attributeClassTypeService.deleteAttributeClassType(id);
    }
}
