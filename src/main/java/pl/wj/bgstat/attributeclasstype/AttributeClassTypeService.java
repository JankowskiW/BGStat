package pl.wj.bgstat.attributeclasstype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeArchivedStatus;
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

    public List<AttributeClassTypeHeaderDto> getAttributeClassTypeHeaders(AttributeClassTypeArchivedStatus archivedStatus) {
        if (archivedStatus.equals(AttributeClassTypeArchivedStatus.ALL)) {
            return attributeClassTypeRepository.findAttributeClassTypeHeaders();
        } else {
            return attributeClassTypeRepository.findAttributeClassTypeHeaders(archivedStatus.equals(AttributeClassTypeArchivedStatus.ARCHIVED));
        }
    }

    public AttributeClassType getSingleAttributeClassType(long id) {
        return attributeClassTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    public AttributeClassType addAttributeClassType(AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        throwExceptionWhenExistsByName(attributeClassTypeRequestDto.getName());
        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public AttributeClassType editAttributeClassType(long id, AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        throwExceptionWhenNotExistsById(id, attributeClassTypeRepository);
        throwExceptionWhenExistsByNameAndNotId(id, attributeClassTypeRequestDto.getName());
        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(id, attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public void deleteAttributeClassType(long id) {
        throwExceptionWhenNotExistsById(id, attributeClassTypeRepository);
        attributeClassTypeRepository.deleteById(id);
    }

    private void throwExceptionWhenExistsByName(String name) {
        if (attributeClassTypeRepository.existsByName(name))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, NAME_FIELD);
    }

    private void throwExceptionWhenExistsByNameAndNotId(long id, String name) {
        if (attributeClassTypeRepository.existsByNameAndIdNot(name, id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, NAME_FIELD);
    }
}
