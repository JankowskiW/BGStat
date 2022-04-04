package pl.wj.bgstat.attributeclass;

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
import pl.wj.bgstat.attributeclass.model.AttributeClass;
import pl.wj.bgstat.attributeclass.model.AttributeClassMapper;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassHeaderDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassRequestDto;
import pl.wj.bgstat.attributeclass.model.dto.AttributeClassResponseDto;
import pl.wj.bgstat.attributeclasstype.model.AttributeClassType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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
    @Description("Should return last page of attrybute class headers")
    void shouldReturnOnlylastPageOfAttrybuteClassHeaders() {
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
    @Description("Should return empty list of attrybute class headers when page number is too hight")
    void shouldReturnEmptylistOfAttrybuteClassTypeHeaders() {
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
    @Description("Should return only one attrbute class details")
    void shouldReturnSingleAttributeClassDeatails() {
        // given
        long id = 1l;
        Optional<AttributeClass> returnedAttributeClass = attributeClassList.stream()
                .filter(ac -> ac.getId() == id)
                .findAny();
        AttributeClassResponseDto expectedResponse = AttributeClassMapper.mapToAttributeClassResponseDto(returnedAttributeClass.orElseThrow());
        given(attributeClassRepository.findById(anyLong())).willReturn(returnedAttributeClass);

        // when
        AttributeClassResponseDto attributeClassResponseDto = attributeClassService.getSingleAttribyteClass(id);

        // then
        assertThat(attributeClassResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
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