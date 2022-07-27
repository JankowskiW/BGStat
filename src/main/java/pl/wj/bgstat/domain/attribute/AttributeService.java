package pl.wj.bgstat.domain.attribute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.domain.attribute.model.Attribute;
import pl.wj.bgstat.domain.attribute.model.AttributeMapper;
import pl.wj.bgstat.domain.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.domain.attribute.model.dto.AttributeResponseDto;
import pl.wj.bgstat.domain.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.domain.attributeclasstype.AttributeClassTypeRepository;
import pl.wj.bgstat.domain.boardgame.BoardGameRepository;
import pl.wj.bgstat.domain.boardgamedescription.BoardGameDescriptionRepository;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.exception.SystemObjectTypeIncompatibilityException;
import pl.wj.bgstat.domain.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.domain.systemobjecttype.enumeration.ObjectType;
import pl.wj.bgstat.domain.userboardgame.UserBoardGameRepository;

import java.util.Arrays;
import java.util.Optional;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class AttributeService {
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
            if (checkIfAttributeExists(attributeRequestDto))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        } else {
            if (checkIfMultivaluedAttributeExists(attributeRequestDto))
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
            if (checkIfOtherAttributeExists(id, attributeRequestDto))
                throw new ResourceExistsException(ATTRIBUTE_RESOURCE_NAME, Optional.empty());
        } else {
            if (checkIfOtherMultivaluedAttributeExists(id, attributeRequestDto))
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

    private boolean checkIfAttributeExists(AttributeRequestDto attributeRequestDto) {
        return attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValue(
                attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                attributeRequestDto.getAttributeClassId(), attributeRequestDto.getValue());
    }

    private boolean checkIfMultivaluedAttributeExists(AttributeRequestDto attributeRequestDto) {
        return attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassId(
                attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                attributeRequestDto.getAttributeClassId());
    }

    private boolean checkIfOtherAttributeExists(long id, AttributeRequestDto attributeRequestDto) {
        return attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueAndIdNot(
                attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                attributeRequestDto.getAttributeClassId(), attributeRequestDto.getValue(), id);
    }

    private boolean checkIfOtherMultivaluedAttributeExists(long id, AttributeRequestDto attributeRequestDto) {
        return attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndIdNot(
                attributeRequestDto.getObjectId(), attributeRequestDto.getObjectTypeId(),
                attributeRequestDto.getAttributeClassId(), id);
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
