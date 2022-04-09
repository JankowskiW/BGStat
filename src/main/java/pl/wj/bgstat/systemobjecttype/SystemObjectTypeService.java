package pl.wj.bgstat.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.SYSTEM_OBJECT_TYPE_EXISTS_EX_MSG;
import static pl.wj.bgstat.exception.ExceptionHelper.SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG;

@Service
@RequiredArgsConstructor
public class SystemObjectTypeService {

    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;

    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders() {
        return systemObjectTypeRepository.findAllSystemObjectTypeHeaders();
    }

    public SystemObjectType getSingleSystemObjectType(long id) {
        return systemObjectTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id));
    }

    public SystemObjectType addSystemObjectType(SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (systemObjectTypeRepository.existsByName(systemObjectTypeRequestDto.getName()))
            throw new EntityExistsException(SYSTEM_OBJECT_TYPE_EXISTS_EX_MSG);
        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return systemObjectType;
    }

    public SystemObjectType editSystemObjectType(long id, SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (!systemObjectTypeRepository.existsById(id))
            throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id);
        if (systemObjectTypeRepository.existsByNameAndIdNot(systemObjectTypeRequestDto.getName(), id))
            throw new EntityExistsException(SYSTEM_OBJECT_TYPE_EXISTS_EX_MSG);

        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(id, systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return systemObjectType;
    }

    public void deleteSystemObjectType(long id) {
        if(!systemObjectTypeRepository.existsById(id))
            throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id);
        // TODO: check if object is related to any attribute class and if it is then throw an exception
        systemObjectTypeRepository.deleteById(id);
    }

    public List<SystemObjectAttributeClassResponseDto> getAllSystemObjectTypeToAttributeClassAssignments(long id) {
        if (!systemObjectTypeRepository.existsById(id)) throw new EntityNotFoundException(SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG + id);
        return systemObjectAttributeClassRepository.findAllResponseDtosBySystemObjectTypeId(id);
    }
}
