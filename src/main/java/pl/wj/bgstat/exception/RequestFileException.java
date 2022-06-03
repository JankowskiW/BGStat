package pl.wj.bgstat.exception;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

public class RequestFileException extends RuntimeException {
    public RequestFileException(String fileTypeName, int minHeight, int maxHeight, int minWidth, int maxWidth, int maxSize) {
        super(createRequestFileExceptionIncorrectSizeMessage(fileTypeName, minHeight, maxHeight, minWidth, maxWidth, maxSize));
    }

    public RequestFileException(String fileName) {
        super(createRequestFileExceptionSaveFailedMessage(fileName));
    }
}
