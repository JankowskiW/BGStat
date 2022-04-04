package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.attributeclass.model.AttributeClass;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute_classes")
public class AttributeClassController {

    private final AttributeClassService attributeClassService;

    @GetMapping("/{id}")
    public AttributeClass getSingleBoardGame(@PathVariable long id) {
        return attributeClassService.getSingleBoardGame(id);
    }
}
