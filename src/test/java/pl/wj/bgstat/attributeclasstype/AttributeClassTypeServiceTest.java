package pl.wj.bgstat.attributeclasstype;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassTypeMapper;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
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

    private static final int PAGE_SIZE = 4;
    private static final int NUMBER_OF_ELEMENTS = 25;

    private List<AttributeClassType> attributeClassTypeList;
    private List<AttributeClassTypeHeaderDto> attributeClassTypeHeaderList;

    @BeforeEach
    void setUp() {
        attributeClassTypeList = AttributeClassTypeServiceTestHelper.populateAttributeClassTypeList(NUMBER_OF_ELEMENTS);
        attributeClassTypeHeaderList = AttributeClassTypeServiceTestHelper.populateAttributeClassTypeHeaderDtoList(attributeClassTypeList);
    }

    @Test
    @Description("Should return only one but not last page of attribute class type headers")
    void shouldReturnOnlyOneButNotLastPageOfAttributeClassTypeHeaders() {
        // given
        int pageNumber = 2;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        given(attributeClassTypeRepository.findAllAttributeClassTypeHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(attributeClassTypeHeaderList.subList(fromIndex, toIndex)));

        // when
        Page<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassTypeHeaderList.subList(fromIndex, toIndex));
    }

    @Test
    @Description("Should return only last page of attribute class type headers")
    void shouldReturnOnlyLastPageOfAttributeClassTypeHeaders() {
        // given
        int lastPageNumber = (int) ceil(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE);
        int lastPageSize = (NUMBER_OF_ELEMENTS - (int) floor(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE) * PAGE_SIZE);
        lastPageSize = lastPageSize == 0 ? PAGE_SIZE : lastPageSize;
        int fromIndex = NUMBER_OF_ELEMENTS - lastPageSize;
        int toIndex = NUMBER_OF_ELEMENTS;
        given(attributeClassTypeRepository.findAllAttributeClassTypeHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(attributeClassTypeHeaderList.subList(fromIndex, toIndex)));

        // when
        Page<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(PageRequest.of(lastPageNumber, PAGE_SIZE));

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .hasSize(lastPageSize)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassTypeHeaderList.subList(fromIndex, toIndex));
    }

    @Test
    @Description("Should return empty list of attribute class type headers when page number is too high")
    void shouldReturnEmptyListOfAttributeClassTypeHeaders() {
        // given
        int tooHighPageNumber = (int) ceil(attributeClassTypeHeaderList.size() / (double) PAGE_SIZE) + 1;
        given(attributeClassTypeRepository.findAllAttributeClassTypeHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));

        // when
        Page<AttributeClassTypeHeaderDto> attributeClassTypeHeaders =
                attributeClassTypeService.getAttributeClassTypeHeaders(PageRequest.of(tooHighPageNumber, PAGE_SIZE));

        // then
        assertThat(attributeClassTypeHeaders)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @Description("Should return only one attribute class type details")
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
                .isEqualTo(returnedAttributeClassType.get());
    }

    @Test
    @Description("Should throw ResourceNotFoundException when cannot find attribute class type by id")
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
    @Description("Should create and return created attribute class type")
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
    @Description("Should throw EntityExistException when attribute class type already exists in database")
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
    @Description("Should edit attribute class type when exists")
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
    @Description("Should throw ResourceNotFoundException when trying to edit non existing attribute class type")
    void shouldThrowExceptionWhenTryingToEditNonExistingAttributeClassType() {
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
    @Description("Should throw ResourceExistsException when trying to set new name that already exists")
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
    @Description("Should remove attribute class type when id exists in database")
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
    @Description("Should throw ResourceNotFoundException when trying to remove non existing attribute class type")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingAttributeClassType() {
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