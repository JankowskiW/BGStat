package pl.wj.bgstat.systemobjecttype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeArchivedStatus;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeResponseDto;

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
class SystemObjectTypeServiceTest {

    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @Mock
    private SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    @InjectMocks
    private SystemObjectTypeService systemObjectTypeService;

    private static final int NUMBER_OF_ELEMENTS = 5;

    private List<SystemObjectType> systemObjectTypeList;
    private List<SystemObjectTypeHeaderDto> systemObjectTypeHeaderList;
    private List<SystemObjectAttributeClassResponseDto> assignedAtrributeClassList;


    @BeforeEach
    void setUp() {
        systemObjectTypeList = SystemObjectTypeServiceTestHelper.populateSystemObjectTypeList(NUMBER_OF_ELEMENTS);
        systemObjectTypeHeaderList = SystemObjectTypeServiceTestHelper.populateSystemObjectTypeHeaderDtoList(systemObjectTypeList);
        assignedAtrributeClassList =
                SystemObjectTypeServiceTestHelper.populateSystemObjectAttributeClassResponseDtoList(NUMBER_OF_ELEMENTS - 1);
    }

    @Test
    @DisplayName("Should return all system object type headers")
    void shouldReturnAllSystemObjectTypeHeaders() {
        // given
        given(systemObjectTypeRepository.findSystemObjectTypeHeaders())
                .willReturn(systemObjectTypeHeaderList);

        // when
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaders =
                systemObjectTypeService.getSystemObjectTypeHeaders(SystemObjectTypeArchivedStatus.ALL);

        // then
        assertThat(systemObjectTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(systemObjectTypeHeaderList);
    }

    @Test
    @DisplayName("Should return only active system object type headers")
    void shouldReturnActiveSystemObjectTypeHeaders() {
        // given
        given(systemObjectTypeRepository.findSystemObjectTypeHeaders(anyBoolean()))
                .willReturn(systemObjectTypeHeaderList.stream().filter(
                        soth -> !soth.isArchived()).collect(Collectors.toList()));

        // when
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaders =
                systemObjectTypeService.getSystemObjectTypeHeaders(SystemObjectTypeArchivedStatus.ARCHIVED);

        // then
        assertThat(systemObjectTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS/2+1)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(systemObjectTypeHeaderList.stream().filter(
                        soth -> !soth.isArchived()).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Should return only archived system object type headers")
    void shouldReturnArchivedSystemObjectTypeHeaders() {
        // given
        given(systemObjectTypeRepository.findSystemObjectTypeHeaders(anyBoolean()))
                .willReturn(systemObjectTypeHeaderList.stream().filter(
                        SystemObjectTypeHeaderDto::isArchived).collect(Collectors.toList()));

        // when
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaders =
                systemObjectTypeService.getSystemObjectTypeHeaders(SystemObjectTypeArchivedStatus.ACTIVE);

        // then
        assertThat(systemObjectTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS/2)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(systemObjectTypeHeaderList.stream().filter(
                        SystemObjectTypeHeaderDto::isArchived).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Should return empty list of system object type headers when there is no records in db")
    void shouldReturnEmptySystemObjectTypeHeaderList() {
        // given
        given(systemObjectTypeRepository.findSystemObjectTypeHeaders())
                .willReturn(new ArrayList<>());

        // when
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaders =
                systemObjectTypeService.getSystemObjectTypeHeaders(SystemObjectTypeArchivedStatus.ALL);

        // then
        assertThat(systemObjectTypeHeaders)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Should return only one system object type details")
    void shouldReturnSingleSystemObjectTypeDetailsById() {
        // given
        long id = 1L;
        Optional<SystemObjectType> systemObjectType = systemObjectTypeList
                .stream()
                .filter(sot -> sot.getId() == id)
                .findAny();
        SystemObjectTypeResponseDto expectedResponse = SystemObjectTypeMapper.mapToSystemObjectTypeResponseDto(systemObjectType.orElseThrow());
        given(systemObjectTypeRepository.findById(anyLong())).willReturn(systemObjectType);

        // when
        SystemObjectTypeResponseDto systemObjectTypeResponseDto = systemObjectTypeService.getSingleSystemObjectType(id);

        // then
        assertThat(systemObjectTypeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cannot find system object type by id")
    void shouldThrowExceptionWhenCannotFindSystemObjectTypeById() {
        // given
        long id = systemObjectTypeList.size() + 1;
        given(systemObjectTypeRepository.findById(anyLong())).willReturn(
                systemObjectTypeList.stream().filter(sot -> sot.getId() == id).findAny());

        // when
        assertThatThrownBy(() -> systemObjectTypeService.getSingleSystemObjectType(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should create and return created system object type")
    void shouldReturnCreatedSystemObjectType() {
        // given
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = SystemObjectTypeServiceTestHelper.createSystemObjectTypeRequestDto(NUMBER_OF_ELEMENTS);
        SystemObjectType expectedResponse = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        expectedResponse.setId(systemObjectTypeList.size()+1);
        given(systemObjectTypeRepository.existsByName(anyString())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getName().equals(systemObjectTypeRequestDto.getName())));
        given(systemObjectTypeRepository.save(any(SystemObjectType.class))).willAnswer(
                i -> {
                    SystemObjectType sot = i.getArgument(0, SystemObjectType.class);
                    sot.setId(expectedResponse.getId());
                    return sot;
                });

        // when
        SystemObjectTypeResponseDto systemObjectTypeResponseDto = systemObjectTypeService.addSystemObjectType(systemObjectTypeRequestDto);

        // then
        assertThat(systemObjectTypeResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when system object type already exists in database")
    void shouldThrowExceptionWhenSystemObjectTypeExists() {
        // given
        String systemObjectTypeName = "Name No. 1";
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = new SystemObjectTypeRequestDto(
                systemObjectTypeName, "DESCRIPTION", false);
        given(systemObjectTypeRepository.existsByName(anyString()))
                .willReturn(systemObjectTypeList.stream().anyMatch(sot -> sot.getName().equals(systemObjectTypeName)));

        // when
        assertThatThrownBy(() -> systemObjectTypeService.addSystemObjectType(systemObjectTypeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, NAME_FIELD));
    }

    @Test
    @DisplayName("Should edit system object type when exists")
    void shouldEditSystemObjectTypeWhenExists() {
        // given
        long id = 1L;
        SystemObjectType systemObjectType = systemObjectTypeList.stream().filter(sot -> sot.getId() == id).findFirst().orElseThrow();
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = SystemObjectTypeRequestDto.builder()
                .name(systemObjectType.getName())
                .description("NEW " + systemObjectType.getDescription())
                .build();
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));
        given(systemObjectTypeRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() != id &&
                        sot.getName().equals(systemObjectTypeRequestDto.getName())));
        given(systemObjectTypeRepository.save(any(SystemObjectType.class))).willAnswer(
                i -> {
                    SystemObjectType sot = i.getArgument(0, SystemObjectType.class);
                    sot.setId(id);
                    return sot;
                });

        // when
        SystemObjectTypeResponseDto systemObjectTypeResponseDto = systemObjectTypeService.editSystemObjectType(id, systemObjectTypeRequestDto);

        // then
        assertThat(systemObjectTypeResponseDto).isNotNull();
        assertThat(systemObjectTypeResponseDto.getId()).isEqualTo(id);
        assertThat(systemObjectTypeResponseDto.getDescription()).isEqualTo(systemObjectTypeRequestDto.getDescription());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit non existing system object type")
    void shouldThrowExceptionWhenTryingToEditNonExistingSystemObjectType() {
        // given
        long id = 100L;
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));

        // when
        assertThatThrownBy(() -> systemObjectTypeService.editSystemObjectType(id, new SystemObjectTypeRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1L;
        SystemObjectType systemObjectType = systemObjectTypeList.stream().filter(sot -> sot.getId() == id).findFirst().orElseThrow();
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = new SystemObjectTypeRequestDto(
                systemObjectTypeList.get(1).getName(), systemObjectType.getDescription(), systemObjectType.isArchived());
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));
        given(systemObjectTypeRepository.existsByNameAndIdNot(anyString(), anyLong()))
                .willReturn(systemObjectTypeList.stream().anyMatch(sot -> sot.getId() != id &&
                        sot.getName().equals(systemObjectTypeRequestDto.getName())));

        // when
        assertThatThrownBy(() -> systemObjectTypeService.editSystemObjectType(id, systemObjectTypeRequestDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, NAME_FIELD));
    }

    @Test
    @DisplayName("Should remove system object type when id exists in database")
    void shouldRemoveSystemObjectTypeWhenIdExists() {
        // given
        long id = 1L;
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));
        willDoNothing().given(systemObjectTypeRepository).deleteById(anyLong());

        // when
        systemObjectTypeService.deleteSystemObjectType(id);

        // then
        verify(systemObjectTypeRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove non existing system object type")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingSystemObjectType() {
        // given
        long id = 100L;
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));

        // when
        assertThatThrownBy(() -> systemObjectTypeService.deleteSystemObjectType(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when trying to remove system object type with assigned attribute classes")
    void shouldThrowExceptionWhenTryingToRemoveSystemObjectTypeWithAssignedAttributeClass() {
        // given
        long id = 1L;
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));
        given(systemObjectAttributeClassRepository.existsBySystemObjectTypeId(anyLong()))
                .willReturn(assignedAtrributeClassList.stream().anyMatch(aac -> aac.getSystemObjectTypeId() == id));

        //when
        assertThatThrownBy(() -> systemObjectTypeService.deleteSystemObjectType(id))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(createResourceExistsExceptionMessage(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD));
    }

    @Test
    @DisplayName("Should return all attribute classes by system object type id")
    void shouldReturnAllAttributeClassesBySystemObjectTypeId() {
         // given
        long id = 1L;
        List<SystemObjectAttributeClassResponseDto> responseDtoList =
                assignedAtrributeClassList.stream()
                        .filter(aac -> aac.getSystemObjectTypeId() == id)
                        .collect(Collectors.toList());
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));
        given(systemObjectAttributeClassRepository.findAllAssignmentsBySystemObjectTypeId(anyLong()))
                .willReturn(responseDtoList);

         // when
        List<SystemObjectAttributeClassResponseDto> assignedSystemObjectTypeList =
                systemObjectTypeService.getAllAttributeClassToSystemObjectTypeAssignments(id);

         // then
        assertThat(assignedSystemObjectTypeList)
                .isNotNull()
                .hasSize(responseDtoList.size())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(responseDtoList);
    }

    @Test
    @DisplayName("Should return empty list of attribute classes")
    void shouldReturnEmptyListOfAttributeClasses() {
        // given
        long id = NUMBER_OF_ELEMENTS;
        List<SystemObjectAttributeClassResponseDto> responseDtoList =
                assignedAtrributeClassList.stream()
                        .filter(aac -> aac.getSystemObjectTypeId() == id)
                        .collect(Collectors.toList());
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));
        given(systemObjectAttributeClassRepository.findAllAssignmentsBySystemObjectTypeId(anyLong()))
                .willReturn(responseDtoList);

        // when
        List<SystemObjectAttributeClassResponseDto> assignedSystemobjectTypeList =
                systemObjectTypeService.getAllAttributeClassToSystemObjectTypeAssignments(id);

        // then
        assertThat(assignedSystemobjectTypeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Should throw EntityNotFoundExpception when trying to get attribute classes of non existing system object type")
    void shouldThrowExceptionWhenTryingToGetAttributeClassesOfNonExistingSystemObjectType() {
        // given
        long id = 100L;
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id));

        // when
        assertThatThrownBy(() -> systemObjectTypeService.getAllAttributeClassToSystemObjectTypeAssignments(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id));
    }
}