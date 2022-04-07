package pl.wj.bgstat.systemobjectattributeclass;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClass;
import pl.wj.bgstat.systemobjectattributeclass.model.SystemObjectAttributeClassMapper;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassRequestDto;
import pl.wj.bgstat.systemobjectattributeclass.model.dto.SystemObjectAttributeClassResponseDto;
import pl.wj.bgstat.systemobjecttype.model.SystemObjectType;

import javax.persistence.EntityExistsException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.exception.ExceptionHelper.SYSTEM_OBJECT_ATTRIBUTE_CLASS_EXISTS_EX_MSG;


@ExtendWith(MockitoExtension.class)
class SystemObjectAttributeClassServiceTest {

    @Mock
    private SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    @InjectMocks
    private SystemObjectAttributeClassService systemObjectAttributeClassService;

    private static final int NUMBER_OF_ELEMENTS = 5;

    private List<AttributeClass> attributeClassList;
    private List<SystemObjectType> systemObjectTypeList;
    private List<SystemObjectAttributeClass> systemObjectAttributeClassList;

    @BeforeEach
    void setUp() {
        attributeClassList = SystemObjectAttributeClassServiceTestHelper.populateAttributeClassList(NUMBER_OF_ELEMENTS);
        systemObjectTypeList = SystemObjectAttributeClassServiceTestHelper.populateSystemObjectTypeList(2 * NUMBER_OF_ELEMENTS);
        systemObjectAttributeClassList = SystemObjectAttributeClassServiceTestHelper
                .populateSystemObjectAttributeClassList(attributeClassList, systemObjectTypeList);
    }

    @Test
    @Description("Should return created assignment of attribute class to system object type")
    void shouldReturnAssignmentOfAttributeClassToSystemObjectType() {
        // given
        long attributeClassId = 1;
        long systemObjectTypeId = 3;
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassServiceTestHelper.createSystemObjectAttributeClassRequestDto(
                        attributeClassId, systemObjectTypeId);
        SystemObjectAttributeClass systemObjectAttributeClass =
                SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClass(systemObjectAttributeClassRequestDto);
        SystemObjectAttributeClassResponseDto expectedResponse =
                SystemObjectAttributeClassMapper.mapToSystemObjectAttributeClassResponseDto(systemObjectAttributeClass);
        given(systemObjectAttributeClassRepository.existsByAttributeClassIdAndSystemObjectTypeId(anyLong(), anyLong()))
                .willReturn(systemObjectAttributeClassList.stream()
                        .filter(soac ->
                                soac.getId().getAttributeClassId() == systemObjectAttributeClassRequestDto.getAttributeClassId() &&
                                soac.getId().getSystemObjectTypeId() == systemObjectAttributeClassRequestDto.getSystemObjectTypeId())
                        .count() > 0);
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
    @Description("Should throw EntityNotFoundException when trying to assign non existing attribute class to system object type")
    void shouldThrowExceptionWhenTryingToAssignNonExistingAttributeClassToSystemObjectType() {
        // given
        long attributeClassId = 1;
        long systemObjectTypeId = 3;
        SystemObjectAttributeClassRequestDto systemObjectAttributeClassRequestDto =
                SystemObjectAttributeClassServiceTestHelper.createSystemObjectAttributeClassRequestDto(
                        attributeClassId, systemObjectTypeId);
        given(systemObjectAttributeClassRepository.existsByAttributeClassIdAndSystemObjectTypeId(anyLong(), anyLong()))
                .willReturn(systemObjectAttributeClassList.stream()
                        .filter(soac ->
                                soac.getId().getAttributeClassId() == systemObjectAttributeClassRequestDto.getAttributeClassId() &&
                                        soac.getId().getSystemObjectTypeId() == systemObjectAttributeClassRequestDto.getSystemObjectTypeId())
                        .count() > 0);

        // when
        assertThatThrownBy(() -> systemObjectAttributeClassService.addSystemObjectAttributeClass(systemObjectAttributeClassRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(SYSTEM_OBJECT_ATTRIBUTE_CLASS_EXISTS_EX_MSG);
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to assign attribute class to non existing system object type")
    void shouldThrowExceptionWhenTryingToAssignAttributeClassToNonExistingSystemObjectType() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should throw EntityExistsException when trying to create existing assignment")
    void shouldThrowExceptionWhenTryingToCreateExistingAssignment() {

    }

    @Test
    @Description("Should remove attribute class from system object type when id exists in database")
    void shouldRemoveAttributeClassFromSystemObjectType() {
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to remove non existing system object type attribute class")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingSystemObjectTypeAttributeClass() {
    }

}