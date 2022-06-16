package pl.wj.bgstat.exception;

import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.createRequestEnumExceptionMessage;

public class RequestEnumException extends RuntimeException {
    public RequestEnumException(String fieldName, List<String> supportedValues) {
        super(createRequestEnumExceptionMessage(fieldName, supportedValues));
    }
}
