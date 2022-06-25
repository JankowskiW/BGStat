package pl.wj.bgstat.attribute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attribute.model.Attribute;
import pl.wj.bgstat.attribute.model.AttributeMapper;
import pl.wj.bgstat.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_RESOURCE_NAME;
import static pl.wj.bgstat.exception.ExceptionHelper.ID_FIELD;

@Service
@RequiredArgsConstructor
public class AttributeService {
    // TODO: 25.06.2022 Do manual tests for rest of services (AttributeClassService and AttributeClassTypeService were tested) 
    private final AttributeRepository attributeRepository;

    public AttributeResponseDto getSingleAttribute(long id) {
        Attribute attribute = attributeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id));
        return AttributeMapper.mapToAttributeResponseDto(attribute);
    }

    public AttributeResponseDto addAttribute(AttributeRequestDto attributeRequestDto) {
        if (attributeRequestDto.isMultivaluedAttributeClassType()) {
            if (attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueNot(
                    attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                    attributeRequestDto.getAttributeClassId(), attributeRequestDto.getValue()))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        } else {
            if (attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassId(
                    attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                    attributeRequestDto.getAttributeClassId()))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        }

        Attribute attribute = AttributeMapper.mapToAttribute(attributeRequestDto);
        attributeRepository.save(attribute);
        return AttributeMapper.mapToAttributeResponseDto(attribute);
    }

    public AttributeResponseDto editAttribute(long id, AttributeRequestDto attributeRequestDto) {
        if (attributeRequestDto.isMultivaluedAttributeClassType()) {
            if (!attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueAndIdNot(
                    attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                    attributeRequestDto.getAttributeClassId(), attributeRequestDto.getValue(), id))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        }
        if (!attributeRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id);
        Attribute attribute = AttributeMapper.mapToAttribute(id, attributeRequestDto);
        attributeRepository.save(attribute);
        return AttributeMapper.mapToAttributeResponseDto(attribute);
    }

    public void deleteAttribute(long id) {
         if (!attributeRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id);
         attributeRepository.deleteById(id);
    }

}
