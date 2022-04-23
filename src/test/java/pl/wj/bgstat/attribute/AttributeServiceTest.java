package pl.wj.bgstat.attribute;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.attribute.model.Attribute;

import java.util.List;

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
        long id = 1;

        // when

        // then
    }

    @Test
    @Description("Should throw ResourceNotFoundException when cannot find attribute in database")
    void shouldThrowExceptionWhenCannotFindAttributeById() {
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

}