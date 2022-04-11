package pl.wj.bgstat.exception;

import javax.persistence.EntityExistsException;

public class ResourceExistsException extends EntityExistsException {
    public ResourceExistsException(String resource, String field) {
        super(ExceptionHelper.createResourceExistsExceptionMessage(resource, field));
    }
}
