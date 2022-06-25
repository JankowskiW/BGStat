package pl.wj.bgstat.exception;

import static pl.wj.bgstat.exception.ExceptionHelper.createSystemObjectTypeIncompatibilityExceptionMessage;

public class SystemObjectTypeIncompatibilityException extends RuntimeException {
    public SystemObjectTypeIncompatibilityException(long objectTypeId) {
        super(createSystemObjectTypeIncompatibilityExceptionMessage(objectTypeId));
    }
}
