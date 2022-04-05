package pl.wj.bgstat.attributeclasstype;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeMapper;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_CLASS_TYPE_EXISTS_EX_MSG;
import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_CLASS_TYPE_NOT_FOUND_EX_MSG;

@Service
@RequiredArgsConstructor
public class AttributeClassTypeService {

    private final AttributeClassTypeRepository attributeClassTypeRepository;

    public Page<AttributeClassTypeHeaderDto> getAttributeClassTypeHeaders(Pageable pageable) {
        return attributeClassTypeRepository.findAllAttributeClassTypeHeaders(pageable);
    }

    public AttributeClassType getSingleAttributeClassType(long id) {
        return attributeClassTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ATTRIBUTE_CLASS_TYPE_NOT_FOUND_EX_MSG + id));
    }

    public AttributeClassType addAttributeClassType(AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        if (attributeClassTypeRepository.existsByName(attributeClassTypeRequestDto.getName()))
            throw new EntityExistsException(ATTRIBUTE_CLASS_TYPE_EXISTS_EX_MSG);
        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public AttributeClassType editAttributeClassType(long id, AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        if (!attributeClassTypeRepository.existsById(id))
            throw new EntityNotFoundException(ATTRIBUTE_CLASS_TYPE_NOT_FOUND_EX_MSG + id);
        if (attributeClassTypeRepository.existsByNameAndIdNot(attributeClassTypeRequestDto.getName(), id))
            throw new EntityExistsException(ATTRIBUTE_CLASS_TYPE_EXISTS_EX_MSG);

        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(id, attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public void deleteAttributeClassType(long id) {
        if(!attributeClassTypeRepository.existsById(id))
            throw new EntityNotFoundException(ATTRIBUTE_CLASS_TYPE_NOT_FOUND_EX_MSG + id);
        // TODO: check if attribute is related to any object type and if it is then throw an exception
        attributeClassTypeRepository.deleteById(id);
    }
}
