package pl.wj.bgstat.systemobjecttype;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-object-types")
public class SystemObjectTypeController {

    private final SystemObjectTypeService systemObjectTypeService;

    @GetMapping("")
    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders() {
        // TODO: Create filter by archived
        return systemObjectTypeService.getSystemObjectTypeHeaders();
    }

    @GetMapping("/{id}")
    public SystemObjectType getSingleSystemObjectType(@PathVariable long id) {
        return systemObjectTypeService.getSingleSystemObjectType(id);
    }

    @PostMapping("")
    public SystemObjectType addSystemObjectType(@RequestBody @Valid SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        return systemObjectTypeService.addSystemObjectType(systemObjectTypeRequestDto);
    }

    @PutMapping("/{id}")
    public SystemObjectType editSystemObjectType(@PathVariable long id,
                               @RequestBody @Valid SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        return systemObjectTypeService.editSystemObjectType(id, systemObjectTypeRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSystemObjectType(@PathVariable long id) {
        systemObjectTypeService.deleteSystemObjectType(id);
    }
}
