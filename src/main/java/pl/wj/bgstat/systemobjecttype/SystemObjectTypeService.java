package pl.wj.bgstat.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeResponseDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
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
                () -> new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id));
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public SystemObjectTypeResponseDto addSystemObjectType(SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (systemObjectTypeRepository.existsByName(systemObjectTypeRequestDto.getName()))
            throw new EntityExistsException(SYSTEM_OBJECT_TYPE_EXISTS_EX_MSG);
        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public SystemObjectTypeResponseDto editSystemObjectType(long id, SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (!systemObjectTypeRepository.existsById(id))
            throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id);
        if (systemObjectTypeRepository.existsByNameAndIdNot(systemObjectTypeRequestDto.getName(), id))
            throw new EntityExistsException(SYSTEM_OBJECT_TYPE_EXISTS_EX_MSG);

        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(id, systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType);
    }

    public void deleteSystemObjectType(long id) {
        if(!systemObjectTypeRepository.existsById(id))
            throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id);
        if(systemObjectAttributeClassRepository.existsBySystemObjectTypeId(id))
            throw new EntityExistsException(DELETE_STATEMENT_CONFLICTED_WITH_REFERENCE_CONSTRAINT);
        systemObjectTypeRepository.deleteById(id);
    }
    public List<SystemObjectAttributeClassResponseDto> getAllAttributeClassToSystemObjectTypeAssignments(long id) {
        if (!systemObjectTypeRepository.existsById(id)) throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id);
        return systemObjectAttributeClassRepository.findAllAssignmentsBySystemObjectTypeId(id);
    }
}
