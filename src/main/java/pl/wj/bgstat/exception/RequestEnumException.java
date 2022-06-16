package pl.wj.bgstat.exception;

import java.util.List;

public class RequestEnumException extends RuntimeException {
    public RequestEnumException(String fieldName, List<String> supportedValues) {
        super(String.format("Unsupported %s value. Supported values: %s", fieldName, supportedValues.toString()));
    }
}
