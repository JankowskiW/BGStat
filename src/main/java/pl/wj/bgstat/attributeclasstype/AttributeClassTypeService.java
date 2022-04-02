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

@Service
@RequiredArgsConstructor
public class AttributeClassTypeService {

    private final AttributeClassTypeRepository attributeClassTypeRepository;

    public Page<AttributeClassTypeHeaderDto> getAttributeClassTypeHeaders(Pageable pageable) {
        return attributeClassTypeRepository.findAllAttributeClassTypeHeaders(pageable);
    }

    public AttributeClassType getSingleAttributeClassType(long id) {
        return attributeClassTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No such attribute class type with id: " + id));
    }

    public AttributeClassType addAttributeClassType(AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        if (attributeClassTypeRepository.existsByName(attributeClassTypeRequestDto.getName()))
            throw new EntityExistsException("Attribute class type with name '" +
                    attributeClassTypeRequestDto.getName() + "' already exists in database");
        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public AttributeClassType editAttributeClassType(long id, AttributeClassTypeRequestDto attributeClassTypeRequestDto) {
        if (!attributeClassTypeRepository.existsById(id))
            throw new EntityNotFoundException("No such attribute class type with id: " + id);
        if (attributeClassTypeRepository.existsByNameAndIdNot(attributeClassTypeRequestDto.getName(), id))
            throw new EntityNotFoundException("Attribute class type with name '" +
                    attributeClassTypeRequestDto.getName() + "' already exists in database");

        AttributeClassType attributeClassType = AttributeClassTypeMapper.mapToAttributeClassType(id, attributeClassTypeRequestDto);
        attributeClassTypeRepository.save(attributeClassType);
        return attributeClassType;
    }

    public void deleteAttributeClassType(long id) {
        if(!attributeClassTypeRepository.existsById(id))
            throw new EntityNotFoundException("No such attribute class type with id: " + id);
        // TODO: check if attribute is related to any object type and if it is then throw an exception
        attributeClassTypeRepository.deleteById(id);
    }
}
