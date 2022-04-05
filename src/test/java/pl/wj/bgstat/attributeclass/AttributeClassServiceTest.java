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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_CLASS_EXISTS_EX_MSG;
import static pl.wj.bgstat.exception.ExceptionHelper.ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG;

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

    @BeforeEach
    void setUp() {
        attributeClassList = AttributeClassServiceTestHelper.populateAttributeClassList(NUMBER_OF_ELEMENTS);
        attributeClassHeaderList = AttributeClassServiceTestHelper.populateAttributeClassHeaderDtoList(attributeClassList);
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
    @Description("Should return last page of attribute class headers")
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
    @Description("Should return empty list of attribute class headers when page number is too hight")
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
    void shouldReturnSingleAttributeClassDeatailsById() {
        // given
        long id = 1l;
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
    @Description("Should throw EntityNotFoundException when cannot find attribute class by id")
    void shouldThrowExceptionWhenCannotFindAttributeClassById() {
        // given
        long id = 100l;
        given(attributeClassRepository.findWithAttributeClassTypeById(anyLong()))
                .willReturn(attributeClassList.stream()
                        .filter(ac -> ac.getId() == id)
                        .findAny());

        // when
        assertThatThrownBy(() -> attributeClassService.getSingleAttributeClass(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG + id);
    }

    @Test
    @Description("Should create and return created attribute class type")
    void shouldReturnCreatedAttributeClass() {
        // given
        AttributeClassRequestDto attributeClassRequestDto = AttributeClassServiceTestHelper.createAttributeClassRequestDto(NUMBER_OF_ELEMENTS);
        AttributeClass attributeClass = AttributeClassMapper.mapToAttributeClass(attributeClassRequestDto);
        attributeClass.setId(attributeClassList.size()+1);
        AttributeClassResponseDto expectedResponse = AttributeClassMapper.mapToAttributeClassResponseDto(attributeClass);
        given(attributeClassRepository.existsByName(anyString())).willReturn(
                attributeClassList.stream()
                        .filter(ac -> ac.getName().equals(attributeClassRequestDto.getName()))
                        .count() > 0);
        given(attributeClassRepository.save(any(AttributeClass.class))).willAnswer(
                i -> {
                    AttributeClass ac = i.getArgument(0, AttributeClass.class);
                    ac.setId(attributeClass.getId());
                    return ac;
                });

        // when
        AttributeClassResponseDto attributeClassResponseDto = attributeClassService.addAttributeClass(attributeClassRequestDto);

        // then
        assertThat(attributeClassResponseDto)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @Description("Should throw EntityExistsException when attrybute class already exists in database")
    void shouldThrowExceptionWhenAttributeClassExists() {
        // given
        String attributeClassName = "Name No. 1";
        AttributeClassRequestDto attributeClassRequestDto = new AttributeClassRequestDto(
                attributeClassName, "New description", 1);
        given(attributeClassRepository.existsByName(anyString()))
                .willReturn(attributeClassList.stream()
                        .filter(ac -> ac.getName().equals(attributeClassName))
                        .count() > 0);

        // when
        assertThatThrownBy(() -> attributeClassService.addAttributeClass(attributeClassRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(ATTRIBUTE_CLASS_EXISTS_EX_MSG);
    }

    @Test
    @Description("Should edit attribute class when exists")
    void shouldEditAttributeClassWhenExists() {
        // given
        long id = 1l;
        AttributeClass attributeClass = attributeClassList.stream().filter(ac -> ac.getId() == id).findFirst().orElseThrow();
        AttributeClassRequestDto attributeClassRequestDto = AttributeClassRequestDto.builder()
                .name(attributeClass.getName())
                .description("NEW DESCRIPTION")
                .build();
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream()
                        .filter(ac -> ac.getId() == id)
                        .count()>0);
        given(attributeClassRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                attributeClassList.stream()
                        .filter(ac -> ac.getId() != id &&
                                ac.getName().equals(attributeClassRequestDto.getName()))
                        .count()>0);
        given(attributeClassRepository.save(any(AttributeClass.class))).willAnswer(
                i -> {
                    AttributeClass ac = i.getArgument(0, AttributeClass.class);
                    ac.setId(id);
                    return ac;
                });

        // when
        AttributeClassResponseDto attributeClassResponseDto =
                attributeClassService.editAttributeClass(id, attributeClassRequestDto);

        // then
        assertThat(attributeClassResponseDto).isNotNull();
        assertThat(attributeClassResponseDto.getId()).isEqualTo(id);
        assertThat(attributeClassResponseDto.getDescription()).isEqualTo(attributeClassRequestDto.getDescription());
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to edit non existing attribute class")
    void shouldThrowExceptionWhenTryingToEditNonExistingAttributeClass() {
        // given
        long id = 100l;
        given(attributeClassRepository.existsById(anyLong()))
                .willReturn(attributeClassList.stream()
                        .filter(ac -> ac.getId() == id)
                        .count() > 0);

        // when
        assertThatThrownBy(() -> attributeClassService.editAttributeClass(id, new AttributeClassRequestDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG + id);
    }

    @Test
    @Description("Should throw EntityExistsException when trying to set new name that already exists")
    void shouldThrowExceptionWhenTryingToSetNameThatAlreadyExists() {
        // given
        long id = 1l;
        AttributeClass attributeClass = attributeClassList.stream().filter(ac -> ac.getId() == id).findFirst().orElseThrow();
        AttributeClassRequestDto attributeClassRequestDto = new AttributeClassRequestDto(
          attributeClassList.get(1).getName(), attributeClass.getDescription(), attributeClass.getAttributeClassType().getId());
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream().filter(ac -> ac.getId() == id).count() > 0);
        given(attributeClassRepository.existsByNameAndIdNot(anyString(), anyLong())).willReturn(
                attributeClassList.stream()
                        .filter(ac -> ac.getId() == id && ac.getName().equals(attributeClass.getName()))
                        .count() > 0);

        // when
        assertThatThrownBy(() -> attributeClassService.editAttributeClass(id, attributeClassRequestDto))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(ATTRIBUTE_CLASS_EXISTS_EX_MSG);
    }

    @Test
    @Description("Should remove attribute class when id exists in database")
    void shouldRemoveAttributeClassWhenIdExists() {
        // given
        long id = 3l;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream()
                        .filter(ac -> ac.getId() == id)
                        .count() > 0);
        willDoNothing().given(attributeClassRepository).deleteById(anyLong());

        // when
        attributeClassService.deleteAttributeClass(id);

        // then
        verify(attributeClassRepository).deleteById(id);
    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to remove non existing attrybute class")
    void shouldThrowExceptionWhenTryingToRemoveNonExistingAttributeClass() {
        // given
        long id = 100l;
        given(attributeClassRepository.existsById(anyLong())).willReturn(
                attributeClassList.stream()
                        .filter(ac -> ac.getId() == id)
                        .count() > 0);

        // then
        assertThatThrownBy(() -> attributeClassService.deleteAttributeClass(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG + id);
    }

}