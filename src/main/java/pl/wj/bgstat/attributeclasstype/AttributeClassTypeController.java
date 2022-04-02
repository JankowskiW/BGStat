package pl.wj.bgstat.attributeclasstype;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute_class_types")
public class AttributeClassTypeController {

    private final AttributeClassTypeService attributeClassTypeService;

    @GetMapping("")
    public Page<AttributeClassTypeHeaderDto> getAttributeClassTypeHeaders(Pageable pageable) {
        // TODO: Create filter by archived
        return attributeClassTypeService.getAttributeClassTypeHeaders(pageable);
    }

    @GetMapping("/{id}")
    public AttributeClassType getSingleAttributeClassType(@PathVariable long id) {
        return attributeClassTypeService.getSingleAttributeClassType(id);
    }

    @PostMapping("")
    public AttributeClassType addAttributeClasssType(@RequestBody @Valid AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
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
