package pl.wj.bgstat.systemobjecttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemObjectTypeService {

    private final SystemObjectTypeRepository systemObjectTypeRepository;

    public List<SystemObjectTypeHeaderDto> getSystemObjectTypeHeaders() {
        return systemObjectTypeRepository.findAllSystemObjectTypeHeaders();
    }

    public SystemObjectType getSingleSystemObjectType(long id) {
        return systemObjectTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No such system object type with id: " + id));
    }

    public SystemObjectType addSystemObjectType(SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (systemObjectTypeRepository.existsByName(systemObjectTypeRequestDto.getName()))
            throw new EntityExistsException("System object type with name '" +
                    systemObjectTypeRequestDto.getName() + "' already exists in database");
        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return systemObjectType;
    }

    public SystemObjectType editSystemObjectType(long id, SystemObjectTypeRequestDto systemObjectTypeRequestDto) {
        if (!systemObjectTypeRepository.existsById(id))
            throw new EntityNotFoundException("No such system object type with id: " + id);
        if (systemObjectTypeRepository.existsByNameAndIdNot(systemObjectTypeRequestDto.getName(), id))
            throw new EntityExistsException("System object type with name '" +
                    systemObjectTypeRequestDto.getName() + "' already exists in database");

        SystemObjectType systemObjectType = SystemObjectTypeMapper.mapToSystemObjectType(id, systemObjectTypeRequestDto);
        systemObjectTypeRepository.save(systemObjectType);
        return systemObjectType;
    }

    public void deleteSystemObjectType(Long id) {
        if(!systemObjectTypeRepository.existsById(id))
            throw new EntityNotFoundException("No such system object type with id: " + id);
        // TODO: check if object is related to any attribute class and if it is than throw an exception
        systemObjectTypeRepository.deleteById(id);
    }
}
