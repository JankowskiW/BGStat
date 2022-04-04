package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AttributeClassService {

    private final AttributeClassRepository attributeClassRepository;


    public AttributeClass getSingleBoardGame(long id) {
        return attributeClassRepository.findWithAttributeClassTypeById(id).orElseThrow(()->new EntityNotFoundException("XXX"));
    }

    public Page<AttributeClassHeaderDto> getAttributeClassHeaders(PageRequest of) {
        throw new NotYetImplementedException();
    }

    public AttributeClassResponseDto getSingleAttribyteClass(long id) {
        throw new NotYetImplementedException();
    }
}
