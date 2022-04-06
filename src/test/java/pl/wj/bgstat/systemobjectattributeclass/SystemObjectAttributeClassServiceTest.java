package pl.wj.bgstat.systemobjectattributeclass;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class SystemObjectAttributeClassServiceTest {

    @Mock
    private SystemObjectAttributeClassRepository systemObjectAttributeClassRepository;
    @InjectMocks
    private SystemObjectAttributeClassService systemObjectAttributeClassService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @Description("Should return attribute classes by system object id")
    void shouldReturnAttributeClassesBySystemObjectId() {

    }

    @Test
    @Description("Should return system objects by attribute class id")
    void shouldReturnSystemObjectsByAttributeClassId() {

    }

}