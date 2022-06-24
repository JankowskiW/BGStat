package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attribute.AttributeRepository;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.AttributeClassMapper;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.attributeclasstype.AttributeClassTypeRepository;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import java.util.List;
import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class AttributeClassService {

    private final AttributeRepository attributeRepository;
    private final AttributeClassRepository attributeClassRepository;
    private final AttributeClassTypeRepository attributeClassTypeRepository;
    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;

    public Page<AttributeClassHeaderDto> getAttributeClassHeaders(Pageable pageable) {
        return attributeClassRepository.findAllAttributeClassHeaders(pageable);
    }

    public AttributeClassResponseDto getSingleAttributeClass(long id) {
        AttributeClass attributeClass = attributeClassRepository.findWithAttributeClassTypeById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public AttributeClassResponseDto addAttributeClass(AttributeClassRequestDto attributeClassRequestDto) {
        throwExceptionWhenExistsByName(attributeClassRequestDto.getName());
        throwExceptionWhenForeignKeyConstraintViolationOccur(
                attributeClassRequestDto.getAttributeClassTypeId(), attributeClassTypeRepository);
        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(attributeClassRequestDto);
        attributeClassRepository.save(attributeClass);
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public AttributeClassResponseDto editAttributeClass(long id, AttributeClassRequestDto attributeClassRequestDto) {
        throwExceptionWhenNotExistsById(id, attributeClassRepository);
        throwExceptionWhenExistsByNameAndNotId(id, attributeClassRequestDto.getName());
        throwExceptionWhenForeignKeyConstraintViolationOccur(
                attributeClassRequestDto.getAttributeClassTypeId(), attributeClassTypeRepository);
        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(id, attributeClassRequestDto);
        attributeClassRepository.save(attributeClass);
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public void deleteAttributeClass(long id) {
        throwExceptionWhenNotExistsById(id, attributeClassRepository);
        throwExceptionWhenRelatedSystemObjectAttributeClassExists(id);
        throwExceptionWhenRelatedAttributeExists(id);
        attributeClassRepository.deleteById(id);
    }

    public List<SystemObjectAttributeClassResponseDto> getAllSystemObjectTypeToAttributeClassAssignments(long id) {
        throwExceptionWhenNotExistsById(id, attributeClassRepository);
        return systemObjectAttributeClassRepository.findAllAssignmentsByAttributeClassId(id);
    }

    private void throwExceptionWhenExistsByName(String name) {
        if (attributeClassRepository.existsByName(name))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_RESOURCE_NAME, Optional.of(NAME_FIELD));
    }

    private void throwExceptionWhenExistsByNameAndNotId(long id, String name) {
        if(attributeClassRepository.existsByNameAndIdNot(name, id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_RESOURCE_NAME, Optional.of(NAME_FIELD));
    }

    private void throwExceptionWhenRelatedSystemObjectAttributeClassExists(long id) {
        if(systemObjectAttributeClassRepository.existsByAttributeClassId(id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_RESOURCE_NAME, SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME);
    }

    private void throwExceptionWhenRelatedAttributeExists(long id) {
        if (attributeRepository.existsByAttributeClassId(id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_RESOURCE_NAME, ATTRIBUTE_RESOURCE_NAME);
    }
}
