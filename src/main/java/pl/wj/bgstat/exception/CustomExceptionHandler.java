package pl.wj.bgstat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ExceptionBody(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
    }

    @ExceptionHandler(ResourceExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionBody handleResourceExistsException(ResourceExistsException e) {
        return new ExceptionBody(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );
    }

    @ExceptionHandler(RequestFileException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionBody handleRequestFileException(RequestFileException e) {
        return new ExceptionBody(
          e.getMessage(),
          HttpStatus.BAD_REQUEST,
          ZonedDateTime.now()
        );
    }

    @ExceptionHandler(UnsupportedFileMediaTypeException.class)
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ExceptionBody handleFileExistsException(UnsupportedFileMediaTypeException e) {
        return new ExceptionBody(
                e.getMessage(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ZonedDateTime.now()
        );
    }
}
