package pl.wj.bgstat.attributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.AttributeClassMapper;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class AttributeClassService {

    private final AttributeClassRepository attributeClassRepository;
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
        if (attributeClassRepository.existsByName(attributeClassRequestDto.getName()))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_RESOURCE_NAME, NAME_FIELD);
        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(attributeClassRequestDto);
        attributeClassRepository.save(attributeClass);
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public AttributeClassResponseDto editAttributeClass(long id, AttributeClassRequestDto attributeClassRequestDto) {
        if(!attributeClassRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
        if(attributeClassRepository.existsByNameAndIdNot(attributeClassRequestDto.getName(), id))
            throw new ResourceExistsException(ATTRIBUTE_CLASS_RESOURCE_NAME, NAME_FIELD);

        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(id, attributeClassRequestDto);
        attributeClassRepository.save(attributeClass);
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public void deleteAttributeClass(long id) {
        if(!attributeClassRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
        if(systemObjectAttributeClassRepository.existsByAttributeClassId(id))
            throw new ResourceExistsException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD);
        attributeClassRepository.deleteById(id);
    }

    public List<SystemObjectAttributeClassResponseDto> getAllSystemObjectTypeToAttributeClassAssignments(long id) {
        if (!attributeClassRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
        return systemObjectAttributeClassRepository.findAllAssignmentsByAttributeClassId(id);
    }
}
