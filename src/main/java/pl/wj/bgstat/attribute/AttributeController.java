package pl.wj.bgstat.attribute;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attributes")
public class AttributeController {

    private final AttributeService attributeService;
}
