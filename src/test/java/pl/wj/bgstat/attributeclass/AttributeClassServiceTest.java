package pl.wj.bgstat.attributeclass;

import org.junit.jupiter.api.BeforeEach;
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

    
}