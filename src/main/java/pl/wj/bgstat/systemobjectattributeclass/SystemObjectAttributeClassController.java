package pl.wj.bgstat.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-object-attribute-classes")
public class SystemObjectAttributeClassController {

    private final SystemObjectAttributeClassService systemObjectAttributeClassService;

}
