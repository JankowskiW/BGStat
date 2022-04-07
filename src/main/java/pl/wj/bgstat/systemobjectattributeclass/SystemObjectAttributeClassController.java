package pl.wj.bgstat.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.web.bind.annotation.*;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-object-attribute-classes")
public class SystemObjectAttributeClassController {

    private final SystemObjectAttributeClassService systemObjectAttributeClassService;


}
