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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_CLASS_EXISTS_EX_MSG;
import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG;

@Service
@RequiredArgsConstructor
public class AttributeClassService {

    private final AttributeClassRepository attributeClassRepository;

    public Page<AttributeClassHeaderDto> getAttributeClassHeaders(Pageable pageable) {
        return attributeClassRepository.findAllAttributeClassHeaders(pageable);
    }

    public AttributeClassResponseDto getSingleAttributeClass(long id) {
        AttributeClass attributeClass = attributeClassRepository.findWithAttributeClassTypeById(id)
                .orElseThrow(() -> new EntityNotFoundException(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG + id));
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public AttributeClassResponseDto addAttributeClass(AttributeClassRequestDto attributeClassRequestDto) {
        if (attributeClassRepository.existsByName(attributeClassRequestDto.getName()))
            throw new EntityExistsException(ATTRIBUTE_CLASS_EXISTS_EX_MSG);
        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(attributeClassRequestDto);
        attributeClassRepository.save(attributeClass);
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public AttributeClassResponseDto editAttributeClass(long id, AttributeClassRequestDto attributeClassRequestDto) {
        if(!attributeClassRepository.existsById(id)) throw new EntityNotFoundException(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG + id);
        if(attributeClassRepository.existsByNameAndIdNot(attributeClassRequestDto.getName(), id))
            throw new EntityExistsException(ATTRIBUTE_CLASS_EXISTS_EX_MSG);

        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(id, attributeClassRequestDto);
        attributeClassRepository.save(attributeClass);
        return AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
    }

    public void deleteAttributeClass(long id) {
        if(!attributeClassRepository.existsById(id)) throw new EntityNotFoundException(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG + id);
        attributeClassRepository.deleteById(id);
    }
}
