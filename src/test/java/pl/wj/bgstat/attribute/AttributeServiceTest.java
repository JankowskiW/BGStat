package pl.wj.bgstat.attribute;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.attribute.model.Attribute;
import pl.wj.bgstat.attribute.model.AttributeMapper;
import pl.wj.bgstat.attribute.model.dto.AttributeRequestDto;
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;
    @InjectMocks
    private AttributeService attributeService;


    private List<Attribute> attributeList;

    @BeforeEach
    void setUp() {
        attributeList = AttributeServiceTestHelper.populateAttributeList();
    }

    @Test
    @Description("Should return attribute by id when exists in database")
    void shouldReturnAttributeByIdWhenExists() {
        // given
        long id = 1L;
        Optional<Attribute> returnedAttribute = attributeList.stream()
                .filter(a -> a.getId() == id)
                .findAny();
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
    @Description("Should throw ResourceNotFoundException when cannot find attribute in database")
    void shouldThrowExceptionWhenCannotFindAttributeById() {
        // given
        long id = 100L;
        given(attributeRepository.findById(anyLong())).willReturn(
                attributeList.stream().filter(a -> a.getId() == id).findAny());

        // when
        assertThatThrownBy(() -> attributeService.getSingleAttribute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(
                        ATTRIBTUE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @Description("Should create and return created attribute of non multivalued attribute type")
    void shouldReturnCreatedAttribute() {
        // given
        long objectId = attributeList.size()+1;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectId, objectTypeId, attributeClassId, "VAL NO. " + (attributeList.size() + 1), false);
        Attribute attribute = AttributeMapper.mapToAttribute(attributeRequestDto);
        attribute.setId(attributeList.size()+1);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(attribute);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassId(anyLong(), anyLong(), anyLong())).willReturn(
                attributeList.stream().anyMatch(a ->
                        a.getObjectTypeId() == objectTypeId &&
                        a.getObjectId() == objectId &&
                        a.getAttributeClassId() == attributeClassId));
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
    @Description("Should create and return created attribute when attribute has multivalued type")
    void shouldReturnCreatedAttributeWhenAttributeHasMultivaluedType() {
        // given
        long objectId = attributeList.size();
        long objectTypeId = 1L;
        long attributeClassId = 3L;
        String value = "VAL No. " + (attributeList.size() + 1);
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectId, objectTypeId, attributeClassId, value, true);
        Attribute attribute = AttributeMapper.mapToAttribute(attributeRequestDto);
        attribute.setId(attributeList.size()+1);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(attribute);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueNot(
                anyLong(), anyLong(), anyLong(), anyString())).willReturn(
                attributeList.stream().anyMatch(a ->
                        a.getObjectTypeId() == objectTypeId &&
                        a.getObjectId() == objectId &&
                        a.getAttributeClassId() == attributeClassId &&
                        a.getValue().equals(value)));
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
    @Description("Should throw ResourceExistsException when cannot add attribute of singlevalued type to object")
    void shouldThrowExceptionWhenCannotAddAttributeOfSinglevaluedType() {
        // given
        long objectId = 1L;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectId, objectTypeId, attributeClassId, "VAL NO. " + (attributeList.size() + 1), false);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassId(anyLong(), anyLong(), anyLong())).willReturn(
                attributeList.stream().anyMatch(a ->
                        a.getObjectTypeId() == objectTypeId &&
                                a.getObjectId() == objectId &&
                                a.getAttributeClassId() == attributeClassId));

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBTUE_RESOURCE_NAME));
    }

    @Test
    @Description("Should throw ResourceExistsException when cannot add attribute of multivalued type to object")
    void shouldThrowExceptionWhenCannotAddAttributeOfMultivaluedType() {
        // given
        long objectId = attributeList.size();
        long objectTypeId = 1L;
        long attributeClassId = 3L;
        String value = "VAL NO. " + attributeList.size();
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectId, objectTypeId, attributeClassId, value, true);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueNot(
                anyLong(), anyLong(), anyLong(), anyString())).willReturn(
                attributeList.stream().anyMatch(a ->
                        a.getObjectTypeId() == objectTypeId &&
                                a.getObjectId() == objectId &&
                                a.getAttributeClassId() == attributeClassId &&
                                !a.getValue().equals(value)));

        // when
        assertThatThrownBy(() -> attributeService.addAttribute(attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBTUE_RESOURCE_NAME));

    }

    @Test
    @Description("Should edit and return edited singlevalued attribute when exists")
    void shouldEditSinglevaluedAttributeWhenExists() {
        // given
        long id = 1L;
        long objectId = 1L;
        long objectTypeId = 1L;
        long attributeClassId = 1L;
        String value = "NEW VAL";
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(
                objectId, objectTypeId, attributeClassId, value, false);
        AttributeResponseDto expectedResponse = AttributeMapper.mapToAttributeResponseDto(attributeRequestDto, id);
        given(attributeRepository.existsById(anyLong())).willReturn(attributeList.stream().anyMatch(a -> a.getId() == id));

        // when
        AttributeResponseDto attributeResponseDto = attributeService.editAttribute(id, attributeRequestDto);

        // then
        assertThat(attributeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @Description("Should throw ResourceNotFoundException when trying to edit non existing attribute")
    void shouldThrowExceptionWhenTryingToEditNonExistingAttribute() {
        // given
        long id = 100L;

        given(attributeRepository.existsById(anyLong())).willReturn(
                attributeList.stream().anyMatch(a -> a.getId() == id));

        // when
        assertThatThrownBy(() -> attributeService.editAttribute(id, new AttributeRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBTUE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @Description("Should throw ResourceExistsException when object has attribute of given class with new value")
    void shouldThrowExceptionWhenObjectHasAttributeOfGivenClassWithNewValue() {
        // given
        long id = attributeList.size();
        long objectId = attributeList.size();
        long objectTypeId = 1L;
        long attributeClassId = 3L;
        String value = "VAL NO. " + attributeList.size();
        AttributeRequestDto attributeRequestDto = new AttributeRequestDto(objectId, objectTypeId, attributeClassId, value, true);
        given(attributeRepository.existsByObjectIdAndObjectTypeIdAndAttributeClassIdAndValueAndIdNot(
                anyLong(), anyLong(), anyLong(), anyString(), anyLong())).willReturn(
                        attributeList.stream().anyMatch(a ->
                                a.getObjectId() == objectId &&
                                a.getObjectTypeId() == objectTypeId &&
                                a.getAttributeClassId() == attributeClassId &&
                                a.getValue().equals(value) &&
                                a.getId() != id));

        // when
        assertThatThrownBy(() -> attributeService.editAttribute(id, attributeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBTUE_RESOURCE_NAME));
    }

    @Test
    @Description("Should remove attribute when id exists in database")
    void shouldRemoveAttributeWhenIdExists() {
        // given
        long id = 1L;
        given(attributeRepository.existsById(id)).willReturn(
                attributeList.stream().anyMatch(a -> a.getId() == id));
        willDoNothing().given(attributeRepository).deleteById(anyLong());

        // when
        attributeService.deleteAttribute(id);

        // then
        verify(attributeRepository).deleteById(id);

    }

    @Test
    @Description("Should throw ResourceNotFoundException when trying to remove non existing attribute")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingAttribute() {
        // given
        long id = 100L;
        given(attributeRepository.existsById(id)).willReturn(
                attributeList.stream().anyMatch(a -> a.getId() == id));

        // then
        assertThatThrownBy(() -> attributeService.deleteAttribute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBTUE_RESOURCE_NAME, ID_FIELD, id));
    }
}