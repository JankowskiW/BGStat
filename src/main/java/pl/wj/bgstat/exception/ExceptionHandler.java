package pl.wj.bgstat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ExceptionBody(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionBody handleResourceExistsException(ResourceExistsException e) {
        return new ExceptionBody(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RequestFileException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionBody handleRequestFileException(RequestFileException e) {
        return new ExceptionBody(
          e.getMessage(),
          HttpStatus.BAD_REQUEST,
          ZonedDateTime.now()
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileExistsException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleFileExistsException(FileExistsException e) {
        return new ExceptionBody(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now()
        );
    }

}
