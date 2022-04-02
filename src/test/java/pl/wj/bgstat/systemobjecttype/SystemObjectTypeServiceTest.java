package pl.wj.bgstat.systemobjecttype;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectTypeMapper;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeHeaderDto;
import pl.wj.bgstat.systemobjecttype.model.dto.SystemObjectTypeRequestDto;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SystemObjectTypeServiceTest {

    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @InjectMocks
    private SystemObjectTypeService systemObjectTypeService;

    private static final int NUMBER_OF_ELEMENTS = 5;

    private List<SystemObjectType> systemObjectTypeList;
    private List<SystemObjectTypeHeaderDto> systemObjectTypeHeaderList;
    private SystemObjectTypeRequestDto systemObjectTypeRequestDto;

    @BeforeEach
    void setUp() {
        systemObjectTypeList = SystemObjectTypeServiceTestHelper.populateSystemObjectTypeList(NUMBER_OF_ELEMENTS);
        systemObjectTypeHeaderList = SystemObjectTypeServiceTestHelper.populateSystemObjectTypeHeaderDtoList(systemObjectTypeList);
        systemObjectTypeRequestDto = SystemObjectTypeServiceTestHelper.createSystemObjectTypeRequestDto(systemObjectTypeList.size());
    }

    @Test
    @Description("Should return all system object type headers")
    void shouldReturnAllSystemObjectTypeHeaders() {
        // given
        given(systemObjectTypeRepository.findAllSystemObjectTypeHeaders()).willReturn(systemObjectTypeHeaderList);

        // when
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaders =
                systemObjectTypeService.getSystemObjectTypeHeaders();

        // then
        assertThat(systemObjectTypeHeaders)
                .isNotNull()
                .hasSize(NUMBER_OF_ELEMENTS)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(systemObjectTypeHeaderList);
    }

    @Test
    @Description("Should return empty list of system object type headers when there is no records in db")
    void shouldReturnEmptySystemObjectTypeHeaderListWhenTableIsEmpty() {
        // given
        given(systemObjectTypeRepository.findAllSystemObjectTypeHeaders())
                .willReturn(new ArrayList<>());

        // when
        List<SystemObjectTypeHeaderDto> systemObjectTypeHeaders =
                systemObjectTypeService.getSystemObjectTypeHeaders();

        // then
        assertThat(systemObjectTypeHeaders)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Description("Should return only one system object type details")
    void shouldReturnSingleSystemObjectTypeDetailsById() {
        // given
        long id = 1l;
        Optional<SystemObjectType> returnedSystemObjectType = systemObjectTypeList
                .stream()
                .filter(sot -> sot.getId() == id)
                .findAny();
        given(systemObjectTypeRepository.findById(anyLong())).willReturn(returnedSystemObjectType);

        // when
        SystemObjectType systemObjectType = systemObjectTypeService.getSingleSystemObjectType(id);

        // then
        assertThat(systemObjectType)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(returnedSystemObjectType.get());
    }

    @Test
    @Description("Should throw EntityNotFoundException when cannot find system object type by id")
    void shouldThrowExceptionWhenCannotFindSystemObjectTypeById() {
        // given
        long id = systemObjectTypeList.size() + 1;
        String exMsg = "No such system object type with id: " + id;
        given(systemObjectTypeRepository.findById(anyLong()))
                .willReturn(systemObjectTypeList.stream()
                        .filter(sot -> sot.getId() == id)
                        .findAny());

        // when
        assertThatThrownBy(() -> systemObjectTypeService.getSingleSystemObjectType(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
    }

    @Test
    @Description("Should create and return created system object type")
    void shouldReturnCreatedSystemObjectType() {
        // given
        SystemObjectType expectedResponse = SystemObjectTypeMapper.mapToSystemObjectType(systemObjectTypeRequestDto);
        expectedResponse.setId(systemObjectTypeList.size()+1);
        given(systemObjectTypeRepository.existsByName(anyString())).willReturn(
            systemObjectTypeList.stream()
                    .filter(sot -> sot.getName().equals(systemObjectTypeRequestDto.getName()))
                    .count() > 0);
        given(systemObjectTypeRepository.save(any(SystemObjectType.class))).willAnswer(
                i -> {
                    SystemObjectType sot = i.getArgument(0, SystemObjectType.class);
                    sot.setId(expectedResponse.getId());
                    return sot;
                });

        // when
        SystemObjectType systemObjectTypeResponse = systemObjectTypeService.addSystemObjectType(systemObjectTypeRequestDto);

        // then
        assertThat(systemObjectTypeResponse)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @Description("Should throw EntityExistsException when system object type already exists in database")
    void shouldThrowExceptionWhenSystemObjectTypeExists() {
        // given
        String systemObjectTypeName = "Name No. 1";
        String exMsg = "System object type with name '" + systemObjectTypeName + "' already exists in database";
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = new SystemObjectTypeRequestDto(
                systemObjectTypeName, "DESCRIPTION", false);
        given(systemObjectTypeRepository.existsByName(anyString()))
                .willReturn(systemObjectTypeList.stream()
                        .filter(sot -> sot.getName().equals(systemObjectTypeName))
                        .count() > 0);

        // when
        assertThatThrownBy(() -> systemObjectTypeService.addSystemObjectType(systemObjectTypeRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(exMsg);
    }

    @Test
    @Description("Should edit system object type when exists")
    void shouldEditSystemObjectTypeWhenExists() {
        // given
        long id = 1l;
        SystemObjectType systemObjectType = systemObjectTypeList.stream().filter(sot -> sot.getId() == id).findFirst().orElseThrow();
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = SystemObjectTypeRequestDto.builder()
                .name(systemObjectType.getName())
                .description("NEW " + systemObjectType.getDescription())
                .build();
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().filter(sot -> sot.getId() == id).count() > 0);
        given(systemObjectTypeRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                systemObjectTypeList.stream().filter(sot -> sot.getId() != id &&
                        sot.getName().equals(systemObjectTypeRequestDto.getName())).count() > 0);
        given(systemObjectTypeRepository.save(any(SystemObjectType.class))).willAnswer(
                i -> {
                    SystemObjectType sot = i.getArgument(0, SystemObjectType.class);
                    sot.setId(id);
                    sot.setDescription(systemObjectTypeRequestDto.getDescription());
                    return sot;
                });

        // when
        SystemObjectType systemObjectTypeResponse = systemObjectTypeService.editSystemObjectType(id, systemObjectTypeRequestDto);

        // then
        assertThat(systemObjectTypeResponse).isNotNull();
        assertThat(systemObjectTypeResponse.getId()).isEqualTo(id);
        assertThat(systemObjectTypeResponse.getDescription()).isEqualTo(systemObjectTypeRequestDto.getDescription());
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to edit non existing system object type")
    void shouldThrowExceptionWhenTryingToEditNonExistingSystemObjectType() {
        // given
        long id = 100l;
        String exMsg = "No such system object type with id: " + id;
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().filter(sot -> sot.getId() == id).count() > 0);

        // when
        assertThatThrownBy(() -> systemObjectTypeService.editSystemObjectType(id, new SystemObjectTypeRequestDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
    }

    @Test
    @Description("Should throw EntityExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1l;
        SystemObjectType systemObjectType = systemObjectTypeList.stream().filter(sot -> sot.getId() == id).findFirst().orElseThrow();
        SystemObjectTypeRequestDto systemObjectTypeRequestDto = new SystemObjectTypeRequestDto(
                systemObjectTypeList.get(1).getName(), systemObjectType.getDescription(), false);
        String exMsg = "System object type with name '" +systemObjectTypeRequestDto.getName() + "' already exists in database";
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream()
                        .filter(sot -> sot.getId() == id)
                        .count() > 0);
        given(systemObjectTypeRepository.existsByNameAndIdNot(anyString(), anyLong()))
                .willReturn(systemObjectTypeList.stream()
                        .filter(sot -> sot.getId() != id &&
                                sot.getName().equals(systemObjectTypeRequestDto.getName()))
                        .count() > 0);

        // when
        assertThatThrownBy(() -> systemObjectTypeService.editSystemObjectType(id, systemObjectTypeRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(exMsg);
    }

    @Test
    @Description("Should remove system object type whe id exists in database")
    void shouldRemoveSystemObjectTypeWhenIdExists() {
        // given
        long id = 1l;
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream()
                        .filter(sot -> sot.getId() == id)
                        .count() > 0);
        willDoNothing().given(systemObjectTypeRepository).deleteById(anyLong());

        // when
        systemObjectTypeService.deleteSystemObjectType(id);

        // then
        verify(systemObjectTypeRepository).deleteById(id);
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to remove non existing system object type")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingSystemObjectType() {
        // given
        long id = 100l;
        String exMsg = "No such system object type with id: " + id;
        given(systemObjectTypeRepository.existsById(anyLong()))
                .willReturn(systemObjectTypeList.stream()
                        .filter(sot -> sot.getId() == id)
                        .count() > 0);

        // when
        assertThatThrownBy(() -> systemObjectTypeService.deleteSystemObjectType(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exMsg);
    }
}