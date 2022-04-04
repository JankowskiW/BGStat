package pl.wj.bgstat.attributeclass;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AttributeClassServiceTest {

    @Mock
    private AttributeClassRepository attributeClassRepository;
    @InjectMocks
    private AttributeClassService attributeClassService;

    private static final int PAGE_SIZE = 4;
    private static final int NUMBER_OF_ELEMENTS = 25;

    private List<AttributeClass> attributeClassList;
    private List<AttributeClassHeaderDto> attributeClassHeaderList;
    private AttributeClassRequestDto attributeClassRequestDto;

    @BeforeEach
    void setUp() {
        attributeClassList = AttributeClassServiceTestHelper.populateAttributeClassList(NUMBER_OF_ELEMENTS);
        attributeClassHeaderList = AttributeClassServiceTestHelper.populateAttributeClassHeaderDtoList(attributeClassList);
        attributeClassRequestDto = AttributeClassServiceTestHelper.createAttributeClassRequestDto(NUMBER_OF_ELEMENTS);
    }

    @Test
    @Description("Should return only one but not last page of attribute class headers")
    void shouldReturnOnlyOneButNotLastPageOfAttributeClassHeaders() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should return last page of attrybute class headers")
    void shouldReturnOnlylastPageOfAttrybuteClassHeaders() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should return empty list of attrybute class headers when page number is too hight")
    void shouldReturnEmptylistOfAttrybuteClassTypeHeaders() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should return only one attrbute class details")
    void shouldReturnSingleAttributeClassDeatails() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should throw EntityNotFoundException when cannot find attrybute class by id")
    void shouldThrowExceptionWhenCannotFindAttributeClassById() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should create and return created attribute class type")
    void shouldReturnCreatedAttributeClass() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should throw EntityNotFoundException when attrybute class already esists in database")
    void shouldThrowExceptionWhenAttributeClassExists() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should edit attribute class when exists")
    void shouldEditAttributeClassWhenExists() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to edit non existing attribute class")
    void shouldThrowExceptionWhenTryingToEditNonExistingAttributeClass() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should throw EntityExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should remove attribute class when id exists in database")
    void shouldRemoveAttrybuteClassWhenIdExists() {
        // given

        // when

        // then
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to remove non existing attrybute class")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingAttributeClass() {
        // given

        // when

        // then
    }

}