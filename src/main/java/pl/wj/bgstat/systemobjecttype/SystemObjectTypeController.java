package pl.wj.bgstat.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system_object_type")
public class SystemObjectTypeController {

    private final SystemObjectTypeService systemObjectTypeService;

    @GetMapping("")
    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders() {
        return systemObjectTypeService.getSystemObjectTypeHeaders();
    }
}
