package pl.wj.bgstat.domain.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.domain.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.domain.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.domain.systemobjecttype.model.SystemObjectTypeArchivedStatus;
import pl.wj.bgstat.domain.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeRequestDto;
import pl.wj.bgstat.domain.systemobjecttype.model.dto.SystemObjectTypeResponseDto;
import pl.wj.bgstat.exception.ForeignKeyConstraintViolationException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class SystemObjectTypeService {

    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;

    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders(SystemObjectTypeArchivedStatus archivedStatus) {
        if (archivedStatus.equals(SystemObjectTypeArchivedStatus.ALL)) {
            return systemObjectTypeRepository.findSystemObjectTypeHeaders();
        } else {
            return systemObjectTypeRepository.findSystemObjectTypeHeaders(archivedStatus.equals(SystemObjectTypeArchivedStatus.ARCHIVED));
        }
    }

    public SystemObjectTypeResponseDto getSingleSystemObjectType(long id) {
        SystemObjectType systemObjectType = systemObjectTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id));
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public SystemObjectTypeResponseDto addSystemObjectType(SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        throwExceptionWhenExistsByName(systemObjectTypeRequestDto.getName());
        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public SystemObjectTypeResponseDto editSystemObjectType(long id, SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        throwExceptionWhenNotExistsById(id, systemObjectTypeRepository);
        throwExceptionWhenExistsByNameAndNotId(id, systemObjectTypeRequestDto.getName());
        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(id, systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public void deleteSystemObjectType(long id) {
        throwExceptionWhenNotExistsById(id, systemObjectTypeRepository);
        throwExceptionWhenSystemObjectTypeHasAssignedAttributeClass(id);
        systemObjectTypeRepository.deleteById(id);
    }

    public List<SystemObjectAttributeClassResponseDto> getAllAttributeClassToSystemObjectTypeAssignments(long id) {
        throwExceptionWhenNotExistsById(id, systemObjectTypeRepository);
        return systemObjectAttributeClassRepository.findAllAssignmentsBySystemObjectTypeId(id);
    }

    private void throwExceptionWhenExistsByName(String name) {
        if (systemObjectTypeRepository.existsByName(name))
            throw new ResourceExistsException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, Optional.of(NAME_FIELD));
    }

    private void throwExceptionWhenExistsByNameAndNotId(long id, String name) {
        if (systemObjectTypeRepository.existsByNameAndIdNot(name, id))
            throw new ResourceExistsException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, Optional.of(NAME_FIELD));
    }

    private void throwExceptionWhenSystemObjectTypeHasAssignedAttributeClass(long id) {
        if(systemObjectAttributeClassRepository.existsBySystemObjectTypeId(id))
            throw new ForeignKeyConstraintViolationException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME,
                    ATTRIBUTE_CLASS_RESOURCE_NAME, id);
    }
}
