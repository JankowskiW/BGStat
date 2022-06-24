package pl.wj.bgstat.systemobjectattributeclass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassId;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassMapper;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.*;

@ExtendWith(MockitoExtension.class)
class SystemObjectAttributeClassServiceTest {

    @Mock
    private SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    @Mock
    private AttributeClassRepository attributeClassRepository;
    @Mock
    private SystemObjectTypeRepository systemObjectTypeRepository;
    @InjectMocks
    private SystemObjectAttributeClassService systemObjectAttributeClassService;

    private static final int NUMBER_OF_ELEMENTS = 5;

    private List<AttributeClass> attributeClassList;
    private List<SystemObjectType> systemObjectTypeList;
    private List<SystemObjectAttributeClass> systemObjectAttributeClassList;

    @BeforeEach
    void setUp() {
        attributeClassList = SystemObjectAttributeClassServiceTestHelper.populateAttributeClassList(NUMBER_OF_ELEMENTS);
        systemObjectTypeList = SystemObjectAttributeClassServiceTestHelper
                .populateSystemObjectTypeList(2 * NUMBER_OF_ELEMENTS);
        systemObjectAttributeClassList = SystemObjectAttributeClassServiceTestHelper
                .populateSystemObjectAttributeClassList(attributeClassList, systemObjectTypeList);
    }

    @Test
    @DisplayName("Should return created assignment of attribute class to system object type")
    void shouldReturnAssignmentOfAttributeClassToSystemObjectType() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 3L);
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassServiceTestHelper.createSystemObjectAttributeClassRequestDto(id);
        SystemObjectAttributeClass systemObjectAttributeClass =
                SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClass(id, systemObjectAttributeClassRequestDto);
        SystemObjectAttributeClassResponseDto expectedResponse =
                SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id.getSystemObjectTypeId()));
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id.getAttributeClassId()));
        given(systemObjectAttributeClassRepository.existsById(any(SystemObjectAttributeClassId.class)))
                .willReturn(systemObjectAttributeClassList.stream().anyMatch(soac -> soac.getId().equals(id)));
        given(systemObjectAttributeClassRepository.save(any(SystemObjectAttributeClass.class))).willAnswer(
           i -> i.getArgument(0, SystemObjectAttributeClass.class));

        // when
        SystemObjectAttributeClassResponseDto systemObjectAttributeClassResponseDto =
                systemObjectAttributeClassService.addSystemObjectAttributeClass(systemObjectAttributeClassRequestDto);

        // then
        assertThat(systemObjectAttributeClassResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to assign not existing attribute class to system object type")
    void shouldThrowExceptionWhenTryingToAssignNotExistingAttributeClassToSystemObjectType() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(100L, 1L);
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassServiceTestHelper.createSystemObjectAttributeClassRequestDto(id);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id.getSystemObjectTypeId()));
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id.getAttributeClassId()));

        // when
        assertThatThrownBy(() -> systemObjectAttributeClassService.addSystemObjectAttributeClass(systemObjectAttributeClassRequestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(createResourceNotFoundExceptionMessage(ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id.getAttributeClassId()));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to assign attribute class to not existing system object type")
    void shouldThrowExceptionWhenTryingToAssignAttributeClassToNotExistingSystemObjectType() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 100L);
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassServiceTestHelper.createSystemObjectAttributeClassRequestDto(id);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id.getSystemObjectTypeId()));

        // when
        assertThatThrownBy(() -> systemObjectAttributeClassService.addSystemObjectAttributeClass(
                systemObjectAttributeClassRequestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_TYPE_RESOURCE_NAME, ID_FIELD, id.getSystemObjectTypeId()));
    }

    @Test
    @DisplayName("Should throw ResourceExistsException when trying to create existing assignment")
    void shouldThrowExceptionWhenTryingToCreateExistingAssignment() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 1L);
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassServiceTestHelper.createSystemObjectAttributeClassRequestDto(id);
        given(systemObjectTypeRepository.existsById(anyLong())).willReturn(
                systemObjectTypeList.stream().anyMatch(sot -> sot.getId() == id.getSystemObjectTypeId()));
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().anyMatch(ac -> ac.getId() == id.getAttributeClassId()));
        given(systemObjectAttributeClassRepository.existsById(any(SystemObjectAttributeClassId.class)))
                .willReturn(systemObjectAttributeClassList.stream().anyMatch(soac -> soac.getId().equals(id)));

        // when
        assertThatThrownBy(() -> systemObjectAttributeClassService.addSystemObjectAttributeClass(
                systemObjectAttributeClassRequestDto))
                    .isInstanceOf(ResourceExistsException.class)
                    .hasMessage(createResourceExistsExceptionMessage(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, Optional.of(ID_FIELD)));
    }

    @Test
    @DisplayName("Should remove attribute class from system object type when assignment exists in database")
    void shouldRemoveAttributeClassFromSystemObjectTypeWhenAssignmentExists() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 1L);
        given(systemObjectAttributeClassRepository.existsById(any(SystemObjectAttributeClassId.class))).willReturn(
                        systemObjectAttributeClassList.stream().anyMatch(
                                soac -> soac.getId().equals(id)));
        willDoNothing().given(systemObjectAttributeClassRepository).deleteById(any(SystemObjectAttributeClassId.class));

        // when
        systemObjectAttributeClassService.deleteSystemObjectAttributeClass(id.getAttributeClassId(), id.getSystemObjectTypeId());

        // then
        verify(systemObjectAttributeClassRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to remove not existing system object type attribute class")
    void shouldThrowExceptionWhenTryingToRemoveNotExistingSystemObjectTypeAttributeClass() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 100L);
        given(systemObjectAttributeClassRepository.existsById(any(SystemObjectAttributeClassId.class)))
                .willReturn(systemObjectAttributeClassList.stream().anyMatch(soac -> soac.getId().equals(id)));

        // when
        assertThatThrownBy(() -> systemObjectAttributeClassService.deleteSystemObjectAttributeClass(
                id.getAttributeClassId(), id.getSystemObjectTypeId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @DisplayName("Should edit attribute type to system object assignment when exists")
    void shouldEditAttributeTypeToSystemObjectAssignmentWhenExists() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 1L);
        SystemObjectAttributeClass systemObjectAttributeClass =
                systemObjectAttributeClassList.stream().filter(soac ->
                        soac.getId().equals(id)).findFirst().orElseThrow();
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassRequestDto.builder()
                        .attributeClassId(id.getAttributeClassId())
                        .systemObjectTypeId(id.getSystemObjectTypeId())
                        .required(systemObjectAttributeClass.isRequired())
                        .classDefaultValue("NEW " + systemObjectAttributeClass.getClassDefaultValue())
                        .build();
        given(systemObjectAttributeClassRepository.existsById(any(SystemObjectAttributeClassId.class)))
                .willReturn(systemObjectAttributeClassList.stream().anyMatch(soac -> soac.getId().equals(id)));
        given(systemObjectAttributeClassRepository.save(any(SystemObjectAttributeClass.class))).willAnswer(
                i -> i.getArgument(0, SystemObjectAttributeClass.class));

        // when
        SystemObjectAttributeClassResponseDto systemObjectAttributeClassResponseDto =
                systemObjectAttributeClassService.editSystemObjectAttributeClass(
                        id.getAttributeClassId(), id.getSystemObjectTypeId(), systemObjectAttributeClassRequestDto);

        // then
        assertThat(systemObjectAttributeClassResponseDto).isNotNull();
        assertThat(systemObjectAttributeClassResponseDto.getAttributeClassId()).isEqualTo(id.getAttributeClassId());
        assertThat(systemObjectAttributeClassResponseDto.getSystemObjectTypeId()).isEqualTo(id.getSystemObjectTypeId());
        assertThat(systemObjectAttributeClassResponseDto.getClassDefaultValue())
                .isEqualTo(systemObjectAttributeClassRequestDto.getClassDefaultValue());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to edit not existing " +
                 "attribute class to system object type assignment")
    void shouldThrowExceptionWhenTryingToEditNotExistingAssignment() {
        // given
        SystemObjectAttributeClassId id = new SystemObjectAttributeClassId(1L, 100L);
        given(systemObjectAttributeClassRepository.existsById(any(SystemObjectAttributeClassId.class)))
                .willReturn(systemObjectAttributeClassList.stream().anyMatch(soac -> soac.getId().equals(id)));

        // when
        assertThatThrownBy(() -> systemObjectAttributeClassService.editSystemObjectAttributeClass(
                id.getAttributeClassId(), id.getSystemObjectTypeId(), new SystemObjectAttributeClassRequestDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(createResourceNotFoundExceptionMessage(SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME, ID_FIELD, id));
    }

}