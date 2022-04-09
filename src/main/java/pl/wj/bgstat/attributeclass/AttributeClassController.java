package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute-classes")
public class AttributeClassController {

    private final AttributeClassService attributeClassService;

    @GetMapping("")
    public Page<AttributeClassHeaderDto> getAttributeClassHeaders(Pageable pageable) {
        return attributeClassService.getAttributeClassHeaders(pageable);
    }

    @GetMapping("/{id}")
    public AttributeClassResponseDto getSingleAttributeClass(@PathVariable long id) {
        return attributeClassService.getSingleAttributeClass(id);
    }

    @PostMapping("")
    public AttributeClassResponseDto addAttributeClass(@RequestBody @Valid AttributeClassRequestDto attributeClassRequestDto) {
        return attributeClassService.addAttributeClass(attributeClassRequestDto);
    }

    @PutMapping("/{id}")
    public AttributeClassResponseDto editAttributeClass(@PathVariable long id, @RequestBody @Valid AttributeClassRequestDto attributeClassRequestDto) {
        return attributeClassService.editAttributeClass(id, attributeClassRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAttributeClass(@PathVariable long id) {
        attributeClassService.deleteAttributeClass(id);
    }

    @GetMapping("/{id}/system-object-types")
    public List<SystemObjectAttributeClassResponseDto> getAllSystemObjectTypeToAttributeClassAssignments(@PathVariable long id) {
       return  attributeClassService.getAllSystemObjectTypeToAttributeClassAssignments(id);
    }

}
