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
    @Description("Should return created assignment of attribute class to system object type")
    void shouldReturnAssignmentOfAttributeClassToSystemObjectType() {

    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to assign non existing attribute class to system object type")
    void shouldThrowExceptionWhenTryingToAssignNonExistingAttributeClassToSystemObjectType() {

    }

    @Test
    @Description("Should throw EntityNotFoundException when trying to assign attribute class to non existing system object type")
    void shouldThrowExceptionWhenTryingToAssignAttributeClassToNonExistingSystemObjectType() {

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