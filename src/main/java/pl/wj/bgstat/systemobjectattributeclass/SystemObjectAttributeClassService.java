package pl.wj.bgstat.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.exception.ForeignKeyConstraintViolationException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassId;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassMapper;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;

import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class SystemObjectAttributeClassService {

    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final AttributeClassRepository attributeClassRepository;

    public SystemObjectAttributeClassResponseDto addSystemObjectAttributeClass(
            SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {

        SystemObjectType systemObjectType = systemObjectTypeRepository
                .findById(systemObjectAttributeClassRequestDto.getSystemObjectTypeId())
                .orElseThrow(() -> new ForeignKeyConstraintViolationException(
                        SYSTEM_OBJECT_TYPE_RESOURCE_NAME,
                        systemObjectAttributeClassRequestDto.getSystemObjectTypeId()));

        AttributeClass attributeClass = attributeClassRepository
                .findById(systemObjectAttributeClassRequestDto.getAttributeClassId())
                .orElseThrow(() -> new ForeignKeyConstraintViolationException(
                        ATTRIBUTE_CLASS_RESOURCE_NAME,
                        systemObjectAttributeClassRequestDto.getAttributeClassId()));

        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(
                systemObjectAttributeClassRequestDto.getAttributeClassId(),
                systemObjectAttributeClassRequestDto.getSystemObjectTypeId());

        throwExceptionWhenExistsByIdAndItsRequired(id, false);
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        systemObjectAttributeClass.setAttributeClass(attributeClass);
        systemObjectAttributeClass.setSystemObjectType(systemObjectType);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }

    public SystemObjectAttributeClassResponseDto editSystemObjectAttributeClass(
            long attributeClassId, long systemObjectTypeId, SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        throwExceptionWhenExistsByIdAndItsRequired(id, true);
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }

    public void deleteSystemObjectAttributeClass(long attributeClassId, long systemObjectTypeId) {
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(attributeClassId, systemObjectTypeId);
        throwExceptionWhenExistsByIdAndItsRequired(id, true);
        systemObjectAttributeClassRepository.deleteById(id);
    }

    private void throwExceptionWhenExistsByIdAndItsRequired(SystemObjectAttributeClassId id, boolean shouldExists) {
        if (shouldExists && !systemObjectAttributeClassRepository.existsById(id))
            throw new ResourceNotFoundException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
        else if (!shouldExists && systemObjectAttributeClassRepository.existsById(id))
            throw new ResourceExistsException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, Optional.of(ID_FIELD));
    }

    private void throwExceptionWhenSystemObjectTypeNotExistsById(long id) {
        if (!systemObjectTypeRepository.existsById(id))
            throw new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id);
    }

    private void throwExceptionWhenAttributeClassNotExistsById(long id) {
        if (!attributeClassRepository.existsById(id))
            throw new ResourceNotFoundException(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id);
    }

}
