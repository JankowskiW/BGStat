package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AttributeClassService {

    private final AttributeClassRepository attributeClassRepository;

    public Page<AttributeClassHeaderDto> getAttributeClassHeaders(PageRequest of) {
        throw new NotYetImplementedException();
    }

    public AttributeClassResponseDto getSingleAttributeClass(long id) {
        throw new NotYetImplementedException();
    }

    public AttributeClassResponseDto addAttributeClass(AttributeClassRequestDto attributeClassRequestDto) {
        throw new NotYetImplementedException();
    }

    public AttributeClassResponseDto editAttributeClass(long id, AttributeClassRequestDto attributeClassRequestDto) {
        throw new NotYetImplementedException();
    }

    public void deleteAttributeClass(long id) {
        throw new NotYetImplementedException();
    }
}
