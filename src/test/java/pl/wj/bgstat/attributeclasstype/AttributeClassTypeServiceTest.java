package pl.wj.bgstat.attributeclasstype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeArchivedStatus;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeMapper;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class AttributeClassTypeServiceTest {

    @Mock
    private AttributeClassTypeRepository attributeClassTypeRepository;
    @InjectMocks
    private AttributeClassTypeService attributeClassTypeService;

    private static final int NUMBER_OF_ELEMENTS = 25;

    private List<AttributeClassType> attributeClassTypeList;
    private List<AttributeClassTypeHeaderDto> attributeClassTypeHeaderList;

    @BeforeEach
    void setUp() {
        attributeClassTypeList = AttributeClassTypeServiceTestHelper.populateAttributeClassTypeList(NUMBER_OF_ELEMENTS);
        attributeClassTypeHeaderList = AttributeClassTypeServiceTestHelper.populateAttributeClassTypeHeaderDtoList(attributeClassTypeList);
    }

    @Test
    @DisplayName("Should return all attribute class type headers")
    void shouldReturnAllAttributeClassTypeHeaders() {
        // given
        given(attributeClassTypeRepository.findAttributeClassTypeHeaders())
                .willReturn(attributeClassTypeHeaderList);

        // when
        List<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(AttributeClassTypeArchivedStatus.ALL);

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassTypeHeaderList);
    }

    @Test
    @DisplayName("Should return only active attribute class type headers")
    void shouldReturnActiveAttributeClassTypeHeaders() {
        // given
        given(attributeClassTypeRepository.findAttributeClassTypeHeaders(anyBoolean()))
                .willReturn(attributeClassTypeHeaderList.stream().filter(
                        acth -> !acth.isArchived()).collect(Collectors.toList()));

        // when
        List<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(AttributeClassTypeArchivedStatus.ACTIVE);

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS/2 + 1)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassTypeHeaderList.stream().filter(
                        acth -> !acth.isArchived()).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Should return only active attribute class type headers")
    void shouldReturnArchivedAttributeClassTypeHeaders() {
        // given
        given(attributeClassTypeRepository.findAttributeClassTypeHeaders(anyBoolean()))
                .willReturn(attributeClassTypeHeaderList.stream().filter(
                        AttributeClassTypeHeaderDto::isArchived).collect(Collectors.toList()));

        // when
        List<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(AttributeClassTypeArchivedStatus.ARCHIVED);

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS/2)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassTypeHeaderList.stream().filter(
                        AttributeClassTypeHeaderDto::isArchived).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Should return empty list of attribute class type headers when there is no records in db")
    void shouldReturnEmptyListOfAttributeClassTypeHeaderList() {
        // given
        given(attributeClassTypeRepository.findAttributeClassTypeHeaders())
                .willReturn(new ArrayList<>());

        // when
        List<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(AttributeClassTypeArchivedStatus.ALL);

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Should return only one attribute class type details")
    void shouldReturnSingleAttributeClassTypeDetails() {
        // given
        long id = 1L;
        Optional<AttributeClassType> returnedAttributeClassType = attributeClassTypeList.stream()
                .filter(act -> act.getId() == id)
                .findAny();
        given(attributeClassTypeRepository.findById(anyLong())).willReturn(returnedAttributeClassType);

        // when
        AttributeClassType attributeClassType = attributeClassTypeService.getSingleAttributeClassType(id);

        // then
        assertThat(attributeClassType)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(returnedAttributeClassType.orElse(null));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cannot find attribute class type by id")
    void shouldThrowExceptionWhenCannotFindAttributeClassTypeById() {
        // given
        long id = attributeClassTypeList.size() + 1;
        given(attributeClassTypeRepository.findById(anyLong())).willReturn(
                attributeClassTypeList.stream().filter(act -> act.getId() == id).findAny());

        // when
        assertThatThrownBy(() -> attributeClassTypeService.getSingleAttributeClassType(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should create and return created attribute class type")
    void shouldReturnCreatedAttributeClassType() {
        // given
        AttributeClassTypeRequestDto attributeClassTypeRequestDto = AttributeClassTypeServiceTestHelper.createAttributeClassTypeRequestDto(NUMBER_OF_ELEMENTS);
        AttributeClassType expectedResponse = AttributeClassTypeMapper.mapToAttributeClassType(attributeClassTypeRequestDto);
        expectedResponse.setId(attributeClassTypeList.size()+1);
        given(attributeClassTypeRepository.existsByName(anyString())).willReturn(
                attributeClassTypeList.stream().anyMatch(act -> act.getName().equals(attributeClassTypeRequestDto.getName())));
        given(attributeClassTypeRepository.save(any(AttributeClassType.class))).willAnswer(
                i -> {
                    AttributeClassType act = i.getArgument(0, AttributeClassType.class);
                    act.setId(expectedResponse.getId());
                    return act;
                });

        // when
        AttributeClassType attributeClassTypeResponse = attributeClassTypeService.addAttributeClassType(attributeClassTypeRequestDto);

        // then
        assertThat(attributeClassTypeResponse)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw EntityExistException when attribute class type already exists in database")
    void shouldThrowExceptionWhenAttributeClassTypeExists() {
        // given
        String attributeClassTypeName = "Name No. 1";
        AttributeClassTypeRequestDto attributeClassTypeRequestDto = new AttributeClassTypeRequestDto(
                attributeClassTypeName, "DESCRIPTION", false);
        given(attributeClassTypeRepository.existsByName(anyString()))
                .willReturn(attributeClassTypeList.stream().anyMatch(act -> act.getName().equals(attributeClassTypeName)));

        // when
        assertThatThrownBy(() -> attributeClassTypeService.addAttributeClassType(attributeClassTypeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, NAME_FIELD));
    }

    @Test
    @DisplayName("Should edit attribute class type when exists")
    void shouldEditAttributeClassTypeWhenExists() {
        // given
        long id = 1L;
        AttributeClassType attributeClassType = attributeClassTypeList.stream().filter(act -> act.getId() == id).findFirst().orElseThrow();
        AttributeClassTypeRequestDto attributeClassTypeRequestDto = AttributeClassTypeRequestDto.builder()
                .name(attributeClassType.getName())
                .description(attributeClassType.getDescription())
                .archived(true)
                .build();
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(
                attributeClassTypeList.stream().anyMatch(act -> act.getId() == id));
        given(attributeClassTypeRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                attributeClassTypeList.stream().anyMatch(act -> act.getId() != id &&
                        act.getName().equals(attributeClassType.getName())));
        given(attributeClassTypeRepository.save(any(AttributeClassType.class))).willAnswer(
                i -> {
                    AttributeClassType act = i.getArgument(0, AttributeClassType.class);
                    act.setId(id);
                    return act;
                });

        // when
        AttributeClassType attributeClassTypeResponse = attributeClassTypeService.editAttributeClassType(id, attributeClassTypeRequestDto);

        // then
        assertThat(attributeClassTypeResponse).isNotNull();
        assertThat(attributeClassTypeResponse.getId()).isEqualTo(id);
        assertThat(attributeClassTypeResponse.isArchived()).isEqualTo(attributeClassTypeRequestDto.isArchived());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit not existing attribute class type")
    void shouldThrowExceptionWhenTryingToEditNotExistingAttributeClassType() {
        // given
        long id = 100L;
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(
                attributeClassTypeList.stream().anyMatch(act -> act.getId() == id));

        // when
        assertThatThrownBy(() -> attributeClassTypeService.editAttributeClassType(id, new AttributeClassTypeRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1L;
        AttributeClassType attributeClassType = attributeClassTypeList.stream().filter(act -> act.getId() == id).findFirst().orElseThrow();
        AttributeClassTypeRequestDto attributeClassTypeRequestDto = new AttributeClassTypeRequestDto(
                attributeClassTypeList.get(1).getName(), attributeClassType.getDescription(), attributeClassType.isArchived());
        given(attributeClassTypeRepository.existsById(anyLong()))
                .willReturn(attributeClassTypeList.stream().anyMatch(act -> act.getId() == id));
        given(attributeClassTypeRepository.existsByNameAndIdNot(anyString(), anyLong()))
                .willReturn(attributeClassTypeList.stream().anyMatch(act ->
                        act.getId() != id &&
                        act.getName().equals(attributeClassTypeRequestDto.getName())));

        // when
        assertThatThrownBy(() -> attributeClassTypeService.editAttributeClassType(id, attributeClassTypeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, NAME_FIELD));
    }

    @Test
    @DisplayName("Should remove attribute class type when id exists in database")
    void shouldRemoveAttributeClassTypeWhenIdExists() {
        // given
        long id = 1L;
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(
                attributeClassTypeList.stream().anyMatch(act -> act.getId() == id));
        willDoNothing().given(attributeClassTypeRepository).deleteById(anyLong());

        // when
        attributeClassTypeService.deleteAttributeClassType(id);

        // then
        verify(attributeClassTypeRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing attribute class type")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingAttributeClassType() {
        // given
        long id = 100L;
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(
                attributeClassTypeList.stream().anyMatch(act -> act.getId() == id));

        // when
        assertThatThrownBy(() -> attributeClassTypeService.deleteAttributeClassType(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }
}