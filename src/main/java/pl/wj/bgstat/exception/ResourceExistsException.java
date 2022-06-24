package pl.wj.bgstat.exception;

import javax.persistence.EntityExistsException;
import java.util.Optional;

public class ResourceExistsException extends EntityExistsException {
    public ResourceExistsException(String resource, Optional<String> field) {
        super(ExceptionHelper.createResourceExistsExceptionMessage(resource, field));
    }
    public ResourceExistsException(String resource, String fkResource) {
        super(ExceptionHelper.createResourceExistsExceptionMessage(resource, fkResource));
    }

}
