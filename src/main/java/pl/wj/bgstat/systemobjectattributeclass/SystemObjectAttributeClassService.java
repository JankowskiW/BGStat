package pl.wj.bgstat.systemobjectattributeclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassMapper;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import javax.persistence.EntityExistsException;

import static pl.wj.bgstat.exception.ExceptionHelper.SYSTEM_OBJECT_ATTRIBUTE_CLASS_EXISTS_EX_MSG;

@Service
@RequiredArgsConstructor
public class SystemObjectAttributeClassService {

    private final SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;

    public SystemObjectAttributeClassResponseDto addSystemObjectAttributeClass(SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto) {
        if (systemObjectAttributeClassRepository.existsByAttributeClassIdAndSystemObjectTypeId(
                systemObjectAttributeClassRequestDto.getAttributeClassId(),
                systemObjectAttributeClassRequestDto.getSystemObjectTypeId()))
                throw new EntityExistsException(SYSTEM_OBJECT_ATTRIBUTE_CLASS_EXISTS_EX_MSG);
        // TODO: Check if FKs exists
        SystemObjectAttributeClass systemObjectAttributeClass = SystemObjectAttributeClassMapper
                .mapToSystemObjectAttributeClass(systemObjectAttributeClassRequestDto);
        systemObjectAttributeClassRepository.save(systemObjectAttributeClass);
        return SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
    }
}
