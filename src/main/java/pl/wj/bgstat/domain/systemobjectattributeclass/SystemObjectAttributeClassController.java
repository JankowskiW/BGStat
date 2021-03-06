package pl.wj.bgstat.domain.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassEditRequestDto;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-object-attribute-classes")
public class SystemObjectAttributeClassController {

    private final SystemObjectAttributeClassService systemObjectAttributeClassService;

    @PostMapping("/{attributeClassId},{systemObjectTypeId}")
    public SystemObjectAttributeClassResponseDto addSystemObjectAttributeClass(
            @RequestBody @Valid SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        return systemObjectAttributeClassService.addSystemObjectAttributeClass(systemObjectAttributeClassRequestDto);
    }

    @PutMapping("/{attributeClassId},{systemObjectTypeId}")
    public SystemObjectAttributeClassResponseDto editSystemObjectAttributeClass(
            @PathVariable long attributeClassId, @PathVariable long systemObjectTypeId,
            @RequestBody @Valid SystemObjectAttributeClassEditRequestDto systemObjectAttributeClassEditRequestDto) {
        return systemObjectAttributeClassService.editSystemObjectAttributeClass(
                attributeClassId, systemObjectTypeId, systemObjectAttributeClassEditRequestDto);
    }

    @DeleteMapping("/{attributeClassId},{systemObjectTypeId}")
    public void deleteSystemObjectAttributeClass(@PathVariable long attributeClassId, @PathVariable long systemObjectTypeId) {
        systemObjectAttributeClassService.deleteSystemObjectAttributeClass(attributeClassId, systemObjectTypeId);
    }
}
