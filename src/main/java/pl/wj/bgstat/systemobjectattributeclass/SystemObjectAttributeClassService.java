package pl.wj.bgstat.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassId;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassMapper;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class SystemObjectAttributeClassService {

    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final AttributeClassRepository attributeClassRepository;


    public SystemObjectAttributeClassResponseDto addSystemObjectAttributeClass(
            SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        if (!systemObjectTypeRepository.existsById(systemObjectAttributeClassRequestDto.getSystemObjectTypeId()))
            throw new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD,
                    + systemObjectAttributeClassRequestDto.getSystemObjectTypeId());
        if (!attributeClassRepository.existsById(systemObjectAttributeClassRequestDto.getAttributeClassId()))
            throw new ResourceNotFoundException(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD,
                    + systemObjectAttributeClassRequestDto.getAttributeClassId());
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(
                systemObjectAttributeClassRequestDto.getAttributeClassId(), systemObjectAttributeClassRequestDto.getSystemObjectTypeId());
        if (systemObjectAttributeClassRepository.existsById(id))
                throw new ResourceExistsException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD);
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }

    public SystemObjectAttributeClassResponseDto editSystemObjectAttributeClass(
            long attributeClassId, long systemObjectTypeId, SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        if (!systemObjectAttributeClassRepository.existsById(id)) throw new ResourceNotFoundException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }

    public void deleteSystemObjectAttributeClass(long attributeClassId, long systemObjectTypeId) {
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        if (!systemObjectAttributeClassRepository.existsById(id))
            throw new ResourceNotFoundException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
        systemObjectAttributeClassRepository.deleteById(id);
    }
}
