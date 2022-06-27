package pl.wj.bgstat.attribute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.attribute.model.Attribute;
import pl.wj.bgstat.attribute.model.AttributeMapper;
import pl.wj.bgstat.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;
import pl.wj.bgstat.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.attributeclasstype.AttributeClassTypeRepository;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgamedescription.BoardGameDescriptionRepository;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.exception.SystemObjectTypeIncompatibilityException;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.systemobjecttype.enumeration.ObjectType;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;

import java.util.Arrays;
import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class AttributeService {
    // TODO: 25.06.2022 Fix unit tests for AttributeService, AttributeClassService and AttributeClassTypeService after changes 
    // TODO: 25.06.2022 Do manual tests for rest of services (AttributeClassService and AttributeClassTypeService were tested)
    private final BoardGameRepository boardGameRepository;
    private final BoardGameDescriptionRepository boardGameDescriptionRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;
    private final AttributeClassRepository attributeClassRepository;
    private final AttributeClassTypeRepository attributeClassTypeRepository;
    private final AttributeRepository attributeRepository;

    public AttributeResponseDto getSingleAttribute(long id) {
        Attribute attribute = attributeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id));
        return AttributeMapper.mapToAttributeResponseDto(attribute);
    }

    public AttributeResponseDto addAttribute(AttributeRequestDto attributeRequestDto) {
        throwExceptionWhenForeignKeyConstraintViolationOccur(attributeRequestDto.getAttributeClassId(), attributeClassRepository);
        throwExceptionWhenForeignKeyConstraintViolationOccur(attributeRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        checkSystemObjectForeignKeyViolation(attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId());
        boolean multivalued = attributeClassTypeRepository
                .getMultivaluedStatusByAttributeClassId(attributeRequestDto.getAttributeClassId());
        if (multivalued) {
            if (attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValue(
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
        if (!attributeRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id);
        throwExceptionWhenForeignKeyConstraintViolationOccur(attributeRequestDto.getAttributeClassId(), attributeClassRepository);
        throwExceptionWhenForeignKeyConstraintViolationOccur(attributeRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        checkSystemObjectForeignKeyViolation(attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId());
        boolean multivalued = attributeClassTypeRepository
                .getMultivaluedStatusByAttributeClassId(attributeRequestDto.getAttributeClassId());
        if (multivalued) {
            if (attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueAndIdNot(
                    attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                    attributeRequestDto.getAttributeClassId(), attributeRequestDto.getValue(), id))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        } else {
            if (attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndIdNot(
                    attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                    attributeRequestDto.getAttributeClassId(), id))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        }
        Attribute attribute = AttributeMapper.mapToAttribute(id, attributeRequestDto);
        attributeRepository.save(attribute);
        return AttributeMapper.mapToAttributeResponseDto(attribute);
    }

    public void deleteAttribute(long id) {
         if (!attributeRepository.existsById(id)) throw new ResourceNotFoundException(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id);
         attributeRepository.deleteById(id);
    }

    private void checkSystemObjectForeignKeyViolation(long objectId, long objectTypeId) {
        ObjectType objectType = Arrays.stream(ObjectType.values()).filter(ot -> ot.getId() == objectTypeId).findFirst()
                .orElseThrow(() -> new SystemObjectTypeIncompatibilityException(objectTypeId));
        if (objectType.equals(ObjectType.BOARD_GAME)) {
            throwExceptionWhenForeignKeyConstraintViolationOccur(objectId, boardGameRepository);
        } else if (objectType.equals(ObjectType.BOARD_GAME_DESCRIPTION)) {
            throwExceptionWhenForeignKeyConstraintViolationOccur(objectId, boardGameDescriptionRepository);
        } else if (objectType.equals(ObjectType.USER_BOARD_GAME)) {
            throwExceptionWhenForeignKeyConstraintViolationOccur(objectId, userBoardGameRepository);
        }
    }
}
