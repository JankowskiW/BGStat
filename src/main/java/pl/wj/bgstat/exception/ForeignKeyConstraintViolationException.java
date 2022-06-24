package pl.wj.bgstat.exception;

import static pl.wj.bgstat.exception.ExceptionHelper.createForeignKeyConstraintViolationExceptionMessage;

public class ForeignKeyConstraintViolationException extends RuntimeException {
    public ForeignKeyConstraintViolationException(String resource, long id) {
        super(createForeignKeyConstraintViolationExceptionMessage(resource, id));
    }
}
