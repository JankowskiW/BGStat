package pl.wj.bgstat.domain.attribute;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.domain.attribute.model.Attribute;
import pl.wj.bgstat.domain.attribute.model.AttributeMapper;
import pl.wj.bgstat.domain.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.domain.attribute.model.dto.AttributeResponseDto;
import pl.wj.bgstat.domain.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.domain.attributeclasstype.AttributeClassTypeRepository;
import pl.wj.bgstat.domain.boardgame.BoardGameRepository;
import pl.wj.bgstat.domain.boardgamedescription.BoardGameDescriptionRepository;
import pl.wj.bgstat.exception.ForeignKeyConstraintViolationException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.exception.SystemObjectTypeIncompatibilityException;
import pl.wj.bgstat.domain.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.domain.systemobjecttype.enumeration.ObjectType;
import pl.wj.bgstat.domain.userboardgame.UserBoardGameRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private BoardGameRepository boardGameRepository;
    @Mock
    private BoardGameDescriptionRepository boardGameDescriptionRepository;
    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @Mock
    private AttributeClassRepository attributeClassRepository;
    @Mock
    private AttributeClassTypeRepository attributeClassTypeRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @InjectMocks
    private AttributeService attributeService;

    @Test
    @DisplayName("Should return attribute by id when exists in database")
    void shouldReturnAttributeByIdWhenExists() {
        // given
        long id = 1L;
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setAttributeClassId(1);
        attribute.setObjectTypeId(1);
        attribute.setObjectId(1);
        attribute.setValue("VALUE");
        Optional<Attribute> returnedAttribute = Optional.of(attribute);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(
                returnedAttribute.orElseThrow());
        given(attributeRepository.findById(anyLong())).willReturn(returnedAttribute);

        // when
        AttributeResponseDto attributeResponseDto = attributeService.getSingleAttribute(id);

        // then
        assertThat(attributeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cannot find attribute in database")
    void shouldThrowExceptionWhenCannotFindAttributeById() {
        // given
        long id = 100L;
        given(attributeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> attributeService.getSingleAttribute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(
                        ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should create and return created attribute when attribute has single valued type")
    void shouldReturnCreatedAttributeWhenAttributeHasSingleValuedType() {
        // given
        long objectId = 10L;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectTypeId, objectId, attributeClassId, "VAL NO. " + objectId);
        Attribute attribute = AttributeMapper.mapToAttribute(attributeRequestDto);
        attribute.setId(objectId);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(attribute);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassTypeRepository.getMultivaluedStatusByAttributeClassId(anyLong())).willReturn(false);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassId(
                anyLong(), anyLong(), anyLong())).willReturn(false);
        given(attributeRepository.save(any(Attribute.class))).willAnswer(
                i -> {
                    Attribute a = i.getArgument(0, Attribute.class);
                    a.setId(attribute.getId());
                    return a;
                });

        // when
        AttributeResponseDto attributeResponseDto = attributeService.addAttribute(attributeRequestDto);

        // then
        assertThat(attributeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should create and return created attribute when attribute has multivalued type")
    void shouldReturnCreatedAttributeWhenAttributeHasMultivaluedType() {
        // given
        long objectId = 10L;
        long objectTypeId = 1L;
        long attributeClassId = 3L;
        String value = "VAL No. " + objectId;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectTypeId,objectId, attributeClassId, value);
        Attribute attribute = AttributeMapper.mapToAttribute(attributeRequestDto);
        attribute.setId(objectId);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(attribute);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassTypeRepository.getMultivaluedStatusByAttributeClassId(anyLong())).willReturn(true);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValue(
                anyLong(), anyLong(), anyLong(), anyString())).willReturn(false);
        given(attributeRepository.save(any(Attribute.class))).willAnswer(
                i -> {
                    Attribute a = i.getArgument(0, Attribute.class);
                    a.setId(attribute.getId());
                    return a;
                });

        // when
        AttributeResponseDto attributeResponseDto = attributeService.addAttribute(attributeRequestDto);

        // then
        assertThat(attributeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when cannot add attribute of single valued type to object")
    void shouldThrowExceptionWhenCannotAddAttributeOfSingleValuedType() {
        // given
        long objectId = 10L;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectTypeId, objectId, attributeClassId, "VAL NO. " + objectId);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassId(
                anyLong(), anyLong(), anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_RESOURCE_NAME, Optional.empty()));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when cannot add attribute of multivalued type to object")
    void shouldThrowExceptionWhenCannotAddAttributeOfMultivaluedType() {
        // given
        long objectId = 10L;
        long objectTypeId = 1L;
        long attributeClassId = 3L;
        String value = "VAL NO. " + objectId;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectTypeId, objectId, attributeClassId, value);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassTypeRepository.getMultivaluedStatusByAttributeClassId(anyLong())).willReturn(true);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValue(
                anyLong(), anyLong(), anyLong(), anyString())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_RESOURCE_NAME, Optional.empty()));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when AtribtueClass Foreign Key Constraint Violation occur")
    void shouldThrowExceptionWhenAttributeClassFKConstraintViolationOccur() {
        // given
        long attributeClassId = 99L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(1, 1, attributeClassId, "");
        given(attributeClassRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, attributeClassId));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when SystemObjectType Foreign Key Constraint Violation occur")
    void shouldThrowExceptionWhenSystemObjectTypeFKConstraintViolationOccur() {
        // given
        long systemObjectTypeId = 99L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(systemObjectTypeId, 2, 3, "");
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, systemObjectTypeId));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when BoardGame object type does not exist in db by id")
    void shouldThrowExceptionWhenBoardGameObjectTypeDoesNotExistById() {
        // given
        long objectId = 1L;
        long systemObjectTypeId = ObjectType.BOARD_GAME.getId();
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(systemObjectTypeId, objectId, 3, "");
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(BOARD_GAME_RESOURCE_NAME, objectId));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when BoardGameDescription object type does not exist in db by id")
    void shouldThrowExceptionWhenBoardGameDescriptionObjectTypeDoesNotExistById() {
        // given
        long objectId = 1L;
        long systemObjectTypeId = ObjectType.BOARD_GAME_DESCRIPTION.getId();
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(systemObjectTypeId, objectId, 3, "");
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameDescriptionRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(BOARD_GAME_DESCRIPTION_RESOURCE_NAME, objectId));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when UserBoardGame object type does not exist in db by id")
    void shouldThrowExceptionWhenUserBoardGameObjectTypeDoesNotExistById() {
        // given
        long objectId = 1L;
        long systemObjectTypeId = ObjectType.USER_BOARD_GAME.getId();
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(systemObjectTypeId, objectId, 3, "");
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(userBoardGameRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(USER_BOARD_GAME_RESOURCE_NAME, objectId));
    }

    @Test
    @DisplayName("Should throw SystemObjectTypeIncompatibilityException when system object type id is not handled")
    void shouldThrowExceptionWhenSystemObjectTypeIdIsNotHandled() {
        // given
        long objectId = 1L;
        long notHandledId = 4L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(notHandledId, objectId, 3, "");
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(SystemObjectTypeIncompatibilityException.class)
                .hasMessage(createSystemObjectTypeIncompatibilityExceptionMessage(notHandledId));
    }

    @Test
    @DisplayName("Should edit and return edited single valued attribute when exists")
    void shouldEditSingleValuedAttributeWhenExists() {
        // given
        long id = 1L;
        long objectId = 1L;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        String value = "NEW VAL";
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectTypeId, objectId, attributeClassId, value);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(attributeRequestDto, id);
        given(attributeRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassTypeRepository.getMultivaluedStatusByAttributeClassId(anyLong())).willReturn(false);
        // when
        AttributeResponseDto attributeResponseDto = attributeService.editAttribute(id, attributeRequestDto);

        // then
        assertThat(attributeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when single valued attribute already exists")
    void shouldThrowExceptionWhenSingleValuedAttributeAlreadyExists() {
        // given
        long id = 1L;
        long objectId = 1L;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        String value = "NEW VAL";
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectTypeId, objectId, attributeClassId, value);
        given(attributeRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassTypeRepository.getMultivaluedStatusByAttributeClassId(anyLong())).willReturn(false);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndIdNot(
                anyLong(), anyLong(), anyLong(), anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeService.editAttribute(id, attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_RESOURCE_NAME, Optional.empty()));

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit not existing attribute")
    void shouldThrowExceptionWhenTryingToEditNotExistingAttribute() {
        // given
        long id = 100L;
        given(attributeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeService.editAttribute(id, new AttributeRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when object has attribute of given class with new value")
    void shouldThrowExceptionWhenObjectHasAttributeOfGivenClassWithNewValue() {
        // given
        long id = 10L;
        long objectId = 10L;
        long objectTypeId = 1L;
        long attributeClassId = 3L;
        String value = "VAL NO. " + id;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(objectTypeId, objectId, attributeClassId, value);
        given(attributeRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(true);
        given(boardGameRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassTypeRepository.getMultivaluedStatusByAttributeClassId(anyLong())).willReturn(true);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueAndIdNot(
                anyLong(), anyLong(), anyLong(), anyString(), anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeService.editAttribute(id, attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_RESOURCE_NAME, Optional.empty()));
    }

    @Test
    @DisplayName("Should remove attribute when id exists in database")
    void shouldRemoveAttributeWhenIdExists() {
        // given
        long id = 1L;
        given(attributeRepository.existsById(id)).willReturn(true);
        willDoNothing().given(attributeRepository).deleteById(anyLong());

        // when
        attributeService.deleteAttribute(id);

        // then
        verify(attributeRepository).deleteById(id);

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing attribute")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingAttribute() {
        // given
        long id = 100L;
        given(attributeRepository.existsById(id)).willReturn(false);

        // then
        assertThatThrownBy(() -> attributeService.deleteAttribute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_RESOURCE_NAME, ID_FIELD, id));
    }
}