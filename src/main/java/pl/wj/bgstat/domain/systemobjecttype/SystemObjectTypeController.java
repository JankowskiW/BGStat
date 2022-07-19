package pl.wj.bgstat.domain.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.domain.systemobjecttype.model.SystemObjectTypeArchivedStatus;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeRequestDto;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-object-types")
public class SystemObjectTypeController {

    private final SystemObjectTypeService systemObjectTypeService;

    @GetMapping("")
    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders(
            @RequestParam(required = false, defaultValue = "ACTIVE") SystemObjectTypeArchivedStatus archivedStatus) {
        return systemObjectTypeService.getSystemObjectTypeHeaders(archivedStatus);
    }

    @GetMapping("/{id}")
    public SystemObjectTypeResponseDto getSingleSystemObjectType(@PathVariable long id) {
        return systemObjectTypeService.getSingleSystemObjectType(id);
    }

    @PostMapping("")
    public SystemObjectTypeResponseDto addSystemObjectType(@RequestBody @Valid SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        return systemObjectTypeService.addSystemObjectType(systemObjectTypeRequestDto);
    }

    @PutMapping("/{id}")
    public SystemObjectTypeResponseDto editSystemObjectType(@PathVariable long id,
                               @RequestBody @Valid SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        return systemObjectTypeService.editSystemObjectType(id, systemObjectTypeRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSystemObjectType(@PathVariable long id) {
        systemObjectTypeService.deleteSystemObjectType(id);
    }

    @GetMapping("/{id}/attribute-classes")
    public List<SystemObjectAttributeClassResponseDto> getAllSystemObjectTypeToAttributeClassAssignments(@PathVariable long id) {
        return systemObjectTypeService.getAllAttributeClassToSystemObjectTypeAssignments(id);
    }
}
