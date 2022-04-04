package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.model.AttributeClass;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AttributeClassService {

    private final AttributeClassRepository attributeClassRepository;


    public AttributeClass getSingleBoardGame(long id) {
        return attributeClassRepository.findWithAttributeClassTypeById(id).orElseThrow(()->new EntityNotFoundException("XXX"));
    }
}
