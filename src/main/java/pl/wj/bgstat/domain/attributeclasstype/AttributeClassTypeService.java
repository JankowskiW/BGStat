package pl.wj.bgstat.domain.attributeclasstype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.domain.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.domain.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.domain.attributeclasstype.model.AttributeClassTypeArchivedStatus;
import pl.wj.bgstat.domain.attributeclasstype.model.AttributeClassTypeMapper;
import pl.wj.bgstat.domain.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.domain.attributeclasstype.model.dto.AttributeClassTypeRequestDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class AttributeClassTypeService {

    private final AttributeClassTypeRepository attributeClassTypeRepository;
    private final AttributeClassRepository attributeClassRepository;

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
        throwExceptionWhenAttributeClassTypeHasRelatedAttributeClass(id);
        attributeClassTypeRepository.deleteById(id);
    }

    private void throwExceptionWhenExistsByName(String name) {
        if (attributeClassTypeRepository.existsByName(name))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, Optional.of(NAME_FIELD));
    }

    private void throwExceptionWhenExistsByNameAndNotId(long id, String name) {
        if (attributeClassTypeRepository.existsByNameAndIdNot(name, id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, Optional.of(NAME_FIELD));
    }

    private void throwExceptionWhenAttributeClassTypeHasRelatedAttributeClass(long id) {
        if (attributeClassRepository.existsByAttributeClassTypeId(id)) {
            throw new ResourceExistsException(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ATTRIBUTE_CLASS_RESOURCE_NAME);
        }
    }
}
