package pl.wj.bgstat.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeResponseDto;

import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class SystemObjectTypeService {

    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;

    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders() {
        return systemObjectTypeRepository.findAllSystemObjectTypeHeaders();
    }

    public SystemObjectTypeResponseDto getSingleSystemObjectType(long id) {
        SystemObjectType systemObjectType = systemObjectTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id));
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public SystemObjectTypeResponseDto addSystemObjectType(SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (systemObjectTypeRepository.existsByName(systemObjectTypeRequestDto.getName()))
            throw new ResourceExistsException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, NAME_FIELD);
        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public SystemObjectTypeResponseDto editSystemObjectType(long id, SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (!systemObjectTypeRepository.existsById(id))
            throw new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id);
        if (systemObjectTypeRepository.existsByNameAndIdNot(systemObjectTypeRequestDto.getName(), id))
            throw new ResourceExistsException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, NAME_FIELD);

        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(id, systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public void deleteSystemObjectType(long id) {
        if(!systemObjectTypeRepository.existsById(id))
            throw new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id);
        if(systemObjectAttributeClassRepository.existsBySystemObjectTypeId(id))
            throw new ResourceExistsException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD);
        systemObjectTypeRepository.deleteById(id);
    }
    public List<SystemObjectAttributeClassResponseDto> getAllAttributeClassToSystemObjectTypeAssignments(long id) {
        if (!systemObjectTypeRepository.existsById(id)) throw new ResourceNotFoundException(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id);
        return systemObjectAttributeClassRepository.findAllAssignmentsBySystemObjectTypeId(id);
    }
}
