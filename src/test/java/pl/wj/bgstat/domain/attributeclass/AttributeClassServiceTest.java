package pl.wj.bgstat.domain.attributeclass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.wj.bgstat.domain.attribute.AttributeRepository;
import pl.wj.bgstat.domain.attributeclass.model.AttributeClass;
import pl.wj.bgstat.domain.attributeclass.model.AttributeClassMapper;
import pl.wj.bgstat.domain.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.domain.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.domain.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.domain.attributeclasstype.AttributeClassTypeRepository;
import pl.wj.bgstat.exception.ExceptionHelper;
import pl.wj.bgstat.exception.ForeignKeyConstraintViolationException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.domain.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.domain.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
class AttributeClassServiceTest {

    @Mock
    private AttributeClassRepository attributeClassRepository;
    @Mock
    private SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private AttributeClassTypeRepository attributeClassTypeRepository;
    @InjectMocks
    private AttributeClassService attributeClassService;

    private static final int PAGE_SIZE = 4;
    private static final int NUMBER_OF_ELEMENTS = 25;

    private List<AttributeClass> attributeClassList;
    private List<AttributeClassHeaderDto> attributeClassHeaderList;
    private List<SystemObjectAttributeClassResponseDto> assignedSystemObjectTypeList;

    @BeforeEach
    void setUp() {
        attributeClassList = AttributeClassServiceTestHelper.populateAttributeClassList(NUMBER_OF_ELEMENTS);
        attributeClassHeaderList = AttributeClassServiceTestHelper.populateAttributeClassHeaderDtoList(attributeClassList);
        assignedSystemObjectTypeList =
                AttributeClassServiceTestHelper.populateSystemObjectAttributeClassResponseDtoList(NUMBER_OF_ELEMENTS - 1);
    }

    @Test
    @DisplayName("Should return only one but not last page of attribute class headers")
    void shouldReturnOnlyOneButNotLastPageOfAttributeClassHeaders() {
        // given
        int pageNumber = 2;
        int fromIndex = (pageNumber - 1) * PAGE_SIZE;
        int toIndex = fromIndex + PAGE_SIZE;
        given(attributeClassRepository.findAllAttributeClassHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(attributeClassHeaderList.subList(fromIndex,toIndex)));

        // when
        Page<AttributeClassHeaderDto> attributeClassHeaders =
                attributeClassService.getAttributeClassHeaders(PageRequest.of(pageNumber, PAGE_SIZE));

        // then
        assertThat(attributeClassHeaders)
                .isNotNull()
                .hasSize(PAGE_SIZE)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassHeaderList.subList(fromIndex,toIndex));
    }

    @Test
    @DisplayName("Should return last page of attribute class headers")
    void shouldReturnOnlyLastPageOfAttributeClassHeaders() {
        // given
        int lastPageNumber = (int) ceil(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE);
        int lastPageSize = (NUMBER_OF_ELEMENTS - (int) floor(NUMBER_OF_ELEMENTS / (double) PAGE_SIZE) * PAGE_SIZE);
        lastPageSize = lastPageSize == 0 ? PAGE_SIZE : lastPageSize;
        int fromIndex = NUMBER_OF_ELEMENTS - lastPageSize;
        int toIndex = NUMBER_OF_ELEMENTS;
        given(attributeClassRepository.findAllAttributeClassHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(attributeClassHeaderList.subList(fromIndex,toIndex)));

        // when
        Page<AttributeClassHeaderDto> attributeClassHeaders =
                attributeClassService.getAttributeClassHeaders(PageRequest.of(lastPageNumber, PAGE_SIZE));

        // then
        assertThat(attributeClassHeaders)
                .isNotNull()
                .hasSize(lastPageSize)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(attributeClassHeaderList.subList(fromIndex,toIndex));
    }

    @Test
    @DisplayName("Should return empty list of attribute class headers when page number is too high")
    void shouldReturnEmptyListOfAttributeClassTypeHeaders() {
        // given
        int tooHighPageNumber = (int) ceil(attributeClassHeaderList.size() / (double) PAGE_SIZE) + 1;
        given(attributeClassRepository.findAllAttributeClassHeaders(any(Pageable.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));

        // when
        Page<AttributeClassHeaderDto> attributeClassHeaders =
                attributeClassService.getAttributeClassHeaders(PageRequest.of(tooHighPageNumber, PAGE_SIZE));

        // then
        assertThat(attributeClassHeaders)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("Should return only one attribute class details")
    void shouldReturnSingleAttributeClassDetailsById() {
        // given
        long id = 1L;
        Optional<AttributeClass> returnedAttributeClass = attributeClassList.stream()
                .filter(ac -> ac.getId() == id)
                .findAny();
        AttributeClassResponseDto expectedResponse = AttributeClassMapper.mapToAttributeClassResponseDto(returnedAttributeClass.orElseThrow());
        given(attributeClassRepository.findWithAttributeClassTypeById(anyLong())).willReturn(returnedAttributeClass);

        // when
        AttributeClassResponseDto attributeClassResponseDto = attributeClassService.getSingleAttributeClass(id);

        // then
        assertThat(attributeClassResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cannot find attribute class by id")
    void shouldThrowExceptionWhenCannotFindAttributeClassById() {
        // given
        long id = 100L;
        given(attributeClassRepository.findWithAttributeClassTypeById(anyLong()))
                .willReturn(attributeClassList.stream()
                        .filter(ac -> ac.getId() == id)
                        .findAny());

        // when
        assertThatThrownBy(() -> attributeClassService.getSingleAttributeClass(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(ExceptionHelper.createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should create and return created attribute class type")
    void shouldReturnCreatedAttributeClass() {
        // given
        String attrClassTypeName = "Example";
        AttributeClassRequestDto attributeClassRequestDto = AttributeClassServiceTestHelper.createAttributeClassRequestDto(NUMBER_OF_ELEMENTS);
        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(attributeClassRequestDto);
        attributeClass.setId(attributeClassList.size()+1);
        AttributeClassResponseDto expectedResponse = AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
        expectedResponse.setAttributeClassTypeName(attrClassTypeName);
        given(attributeClassRepository.existsByName(anyString())).willReturn(false);
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.save(any(AttributeClass.class))).willAnswer(
                i -> {
                    AttributeClass ac = i.getArgument(0, AttributeClass.class);
                    ac.setId(attributeClass.getId());
                    return ac;
                });
        given(attributeClassTypeRepository.getNameById(anyLong())).willReturn(attrClassTypeName);

        // when
        AttributeClassResponseDto attributeClassResponseDto = attributeClassService.addAttributeClass(attributeClassRequestDto);

        // then
        assertThat(attributeClassResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when attribute class already exists in database")
    void shouldThrowExceptionWhenAttributeClassExists() {
        // given
        String attributeClassName = "Name No. 1";
        AttributeClassRequestDto attributeClassRequestDto = new AttributeClassRequestDto(
                attributeClassName, "New description", 1);
        given(attributeClassRepository.existsByName(anyString())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeClassService.addAttributeClass(attributeClassRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, Optional.of(NAME_FIELD)));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when trying to add attribute class and given " +
            "attribute class type id does not exist in database")
    void shouldThrowExceptionWhenTryingToAddAttributeClassAndGivenAttributeClassTypeDoesNotExist() {
        // given
        AttributeClassRequestDto attributeClassRequestDto = AttributeClassServiceTestHelper.createAttributeClassRequestDto(NUMBER_OF_ELEMENTS);
        given(attributeClassRepository.existsByName(anyString())).willReturn(false);
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeClassService.addAttributeClass(attributeClassRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(
                        ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, attributeClassRequestDto.getAttributeClassTypeId()));
    }

    @Test
    @DisplayName("Should edit attribute class when exists")
    void shouldEditAttributeClassWhenExists() {
        // given
        long id = 1L;
        String attrClassTypeName = "Example";
        AttributeClass attributeClass = attributeClassList.stream().filter(ac -> ac.getId() == id).findFirst().orElseThrow();
        AttributeClassRequestDto attributeClassRequestDto = AttributeClassRequestDto.builder()
                .name(attributeClass.getName())
                .description("NEW DESCRIPTION")
                .build();
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(false);
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.save(any(AttributeClass.class))).willAnswer(
                i -> {
                    AttributeClass ac = i.getArgument(0, AttributeClass.class);
                    ac.setId(id);
                    return ac;
                });
        given(attributeClassTypeRepository.getNameById(anyLong())).willReturn(attrClassTypeName);

        // when
        AttributeClassResponseDto attributeClassResponseDto =
                attributeClassService.editAttributeClass(id, attributeClassRequestDto);

        // then
        assertThat(attributeClassResponseDto).isNotNull();
        assertThat(attributeClassResponseDto.getId()).isEqualTo(id);
        assertThat(attributeClassResponseDto.getDescription()).isEqualTo(attributeClassRequestDto.getDescription());
        assertThat(attributeClassResponseDto.getAttributeClassTypeName()).isEqualTo(attrClassTypeName);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit not existing attribute class")
    void shouldThrowExceptionWhenTryingToEditNotExistingAttributeClass() {
        // given
        long id = 100L;
        given(attributeClassRepository.existsById(anyLong())) .willReturn(false);

        // when
        assertThatThrownBy(() -> attributeClassService.editAttributeClass(id, new AttributeClassRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1L;
        AttributeClass attributeClass = attributeClassList.stream().filter(ac -> ac.getId() == id).findFirst().orElseThrow();
        AttributeClassRequestDto attributeClassRequestDto = new AttributeClassRequestDto(
          attributeClassList.get(1).getName(), attributeClass.getDescription(), attributeClass.getAttributeClassType().getId());
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeClassService.editAttributeClass(id, attributeClassRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, Optional.of(NAME_FIELD)));
    }

    @Test
    @DisplayName("Should throw ForeignKeyConstraintViolationException when trying to set Attribute Class Type that does not exist")
    void shouldThrowExceptionWhenTryingToSetAttributeClassTypeThatDoesNotExist() {
        // given
        long id = 1L;
        AttributeClass attributeClass = attributeClassList.stream().filter(ac -> ac.getId() == id).findFirst().orElseThrow();
        AttributeClassRequestDto attributeClassRequestDto = new AttributeClassRequestDto(
                attributeClassList.get(1).getName(), attributeClass.getDescription(), attributeClass.getAttributeClassType().getId());
        given(attributeClassRepository.existsById(anyLong())).willReturn(true);
        given(attributeClassRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(false);
        given(attributeClassTypeRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThatThrownBy(() -> attributeClassService.editAttributeClass(id, attributeClassRequestDto))
                .isInstanceOf(ForeignKeyConstraintViolationException.class)
                .hasMessage(createForeignKeyConstraintViolationExceptionMessage(
                        ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME, attributeClassRequestDto.getAttributeClassTypeId()));
    }

    @Test
    @DisplayName("Should remove attribute class when id exists in database")
    void shouldRemoveAttributeClassWhenIdExists() {
        // given
        long id = 3L;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));
        given(systemObjectAttributeClassRepository.existsByAttributeClassId(anyLong())).willReturn(false);
        given(attributeRepository.existsByAttributeClassId(anyLong())).willReturn(false);
        willDoNothing().given(attributeClassRepository).deleteById(anyLong());

        // when
        attributeClassService.deleteAttributeClass(id);

        // then
        verify(attributeClassRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when related system object attribute class exists")
    void shouldThrowExceptionWhenRelatedSystemObjectAttributeClassExists() {
        // given
        long id = 3L;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));
        given(systemObjectAttributeClassRepository.existsByAttributeClassId(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeClassService.deleteAttributeClass(id))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when related attribute exists")
    void shouldThrowExceptionWhenRelatedAttributeExists() {
        // given
        long id = 3L;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));
        given(systemObjectAttributeClassRepository.existsByAttributeClassId(anyLong())).willReturn(false);
        given(attributeRepository.existsByAttributeClassId(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> attributeClassService.deleteAttributeClass(id))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, ATTRIBUTE_RESOURCE_NAME));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing attribute class")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingAttributeClass() {
        // given
        long id = 100L;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));

        // then
        assertThatThrownBy(() -> attributeClassService.deleteAttributeClass(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should return all system object types by attribute class id")
    void shouldReturnAllSystemObjectTypesByAttributeClassId() {
        // given
        long id = 1L;
        List<SystemObjectAttributeClassResponseDto> responseDtoList =
                assignedSystemObjectTypeList.stream()
                    .filter(asot -> asot.getAttributeClassId() == id)
                    .collect(Collectors.toList());
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));
        given(systemObjectAttributeClassRepository.findAllAssignmentsByAttributeClassId(anyLong()))
                .willReturn(responseDtoList);

        // when
        List<SystemObjectAttributeClassResponseDto> assignedAttributeClassList =
                attributeClassService.getAllSystemObjectTypeToAttributeClassAssignments(id);

        // then
        assertThat(assignedAttributeClassList)
                .isNotNull()
                .hasSize(responseDtoList.size())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(responseDtoList);

    }

    @Test
    @DisplayName("Should return empty list of system object types")
    void shouldReturnEmptyListOfSystemObjectTypes() {
        // given
        long id = NUMBER_OF_ELEMENTS;
        List<SystemObjectAttributeClassResponseDto> responseDtoList =
                assignedSystemObjectTypeList.stream()
                        .filter(asot -> asot.getAttributeClassId() == id)
                        .collect(Collectors.toList());
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));
        given(systemObjectAttributeClassRepository.findAllAssignmentsByAttributeClassId(anyLong()))
                .willReturn(responseDtoList);

        // when
        List<SystemObjectAttributeClassResponseDto> assignedAttributeClassList =
                attributeClassService.getAllSystemObjectTypeToAttributeClassAssignments(id);

        // then
        assertThat(assignedAttributeClassList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to get system object types of not existing attribute class")
    void shouldThrowExceptionWhenTryingToGetSystemObjectTypesOfNotExistingAttributeClass() {
        // given
        long id = 100L;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id));

        // when
        assertThatThrownBy(() -> attributeClassService.getAllSystemObjectTypeToAttributeClassAssignments(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
    }

}