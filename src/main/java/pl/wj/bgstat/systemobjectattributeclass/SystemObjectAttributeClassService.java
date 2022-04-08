package pl.wj.bgstat.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassId;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassMapper;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class SystemObjectAttributeClassService {

    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final AttributeClassRepository attributeClassRepository;

    public SystemObjectAttributeClassResponseDto addSystemObjectAttributeClass(
            long attributeClassId, long systemObjectTypeId,
            SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        if (!systemObjectTypeRepository.existsById(systemObjectTypeId))
            throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG
                    + systemObjectAttributeClassRequestDto.getSystemObjectTypeId());
        if (!attributeClassRepository.existsById(attributeClassId))
            throw new EntityNotFoundException(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG
                    + systemObjectAttributeClassRequestDto.getAttributeClassId());
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        if (systemObjectAttributeClassRepository.existsById(id))
                throw new EntityExistsException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_EXISTS_EX_MSG);
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }

    public void deleteSystemObjectAttributeClass(long attributeClassId, long systemObjectTypeId) {
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        if (!systemObjectAttributeClassRepository.existsById(id))
            throw new EntityNotFoundException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG);
        systemObjectAttributeClassRepository.deleteById(id);
    }

    public SystemObjectAttributeClassResponseDto editSystemObjectAttributeClass(
            long attributeClassId, long systemObjectTypeId, SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        if (!systemObjectAttributeClassRepository.existsById(id)) throw new EntityNotFoundException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG);
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }
}
