package pl.wj.bgstat.attributeclasstype;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeMapper;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class AttributeClassTypeService {

    private final AttributeClassTypeRepository attributeClassTypeRepository;

    public List<AttributeClassTypeHeaderDto> getAttributeClassTypeHeaders() {
        return attributeClassTypeRepository.findAllAttributeClassTypeHeaders();
    }

    public AttributeClassType getSingleAttributeClassType(long id) {
        return attributeClassTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    public AttributeClassType addAttributeClassType(AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        if (attributeClassTypeRepository.existsByName(attributeClassTypeRequestDto.getName()))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, NAME_FIELD);
        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public AttributeClassType editAttributeClassType(long id, AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        if (!attributeClassTypeRepository.existsById(id))
            throw new ResourceNotFoundException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id);
        if (attributeClassTypeRepository.existsByNameAndIdNot(attributeClassTypeRequestDto.getName(), id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, NAME_FIELD);

        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(id, attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public void deleteAttributeClassType(long id) {
        if(!attributeClassTypeRepository.existsById(id))
            throw new ResourceNotFoundException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id);
        attributeClassTypeRepository.deleteById(id);
    }
}
