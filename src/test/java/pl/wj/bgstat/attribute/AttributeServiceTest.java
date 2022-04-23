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
import pl.wj.bgstat.attribute.model.dto.AttributeResponseDto;
import pl.wj.bgstat.exception.ExceptionHelper;
import pl.wj.bgstat.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static pl.wj.bgstat.exception.ExceptionHelper.ID_FIELD;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;
    @InjectMocks
    private AttributeService attributeService;

    private static final int NUMBER_OF_ELEMENTS = 20;

    private List<Attribute> attributeList;

    @BeforeEach
    void setUp() {
        attributeList = AttributeServiceTestHelper.populateAttributeList(NUMBER_OF_ELEMENTS);
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
                .hasMessage(ExceptionHelper.createResourceNotFoundExceptionMessage(
                        ExceptionHelper.ATTRIBTUE_RESOURCE_NAME, ID_FIELD, id));
    }

    @Test
    @Description("Should create and return created attribute")
    void shouldReturnCreatedAttribute() {
    }

    @Test
    @Description("ShouldCreateAttributeAndIncrementOrdinalNumberWhenAttributeTypeIsList")
    void shouldCreateAttributeAndIncrementOrdinalNumberWhenAttributeTypeIsList() {
    }

    @Test
    @Description("Should throw ResourceExistsException when cannot add given attribute to object")
    void shouldThrowExceptionWhenCannotAddGivenAttributeToObject() {
    }

    @Test
    @Description("Should edit and return edited attribute when exists")
    void shouldAttributeWhenExists() {
    }

    @Test
    @Description("Should throw ResourceNotFoundException when trying to edit non existing attribute")
    void shouldThrowExceptionWhenTryingToEditNonExistingAttribute() {
    }

    @Test
    @Description("Should remove attribute when id exists in database")
    void shouldRemoveAttributeWhenIdExists() {

    }

    @Test
    @Description("Should throw ResourceNotFoundException when trying to remove non existing attribute")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingAttribute() {

    }



}