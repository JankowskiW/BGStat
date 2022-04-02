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
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeHeaderDto;
import pl.wj.bgstat.attributeclasstype.model.dto.AttributeClassTypeRequestDto;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    private AttributeClassTypeRequestDto attributeClassTypeRequestDto;

    @BeforeEach
    void setUp() {
        attributeClassTypeList = AttributeClassTypeServiceTestHelper.populateAttributeClassTypeList(NUMBER_OF_ELEMENTS);
        attributeClassTypeHeaderList = AttributeClassTypeServiceTestHelper.populateAttributeClassTypeHeaderDtoList(attributeClassTypeList);
        attributeClassTypeRequestDto = AttributeClassTypeServiceTestHelper.createAttributeClassTypeRequestDto(NUMBER_OF_ELEMENTS);
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
}