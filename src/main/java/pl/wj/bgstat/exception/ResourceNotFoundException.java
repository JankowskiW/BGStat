package pl.wj.bgstat.exception;

import javax.persistence.EntityNotFoundException;

public class ResourceNotFoundException extends EntityNotFoundException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(ExceptionHelper.createResourceNotFoundExceptionMessage(resource, field, value));
    }
}
