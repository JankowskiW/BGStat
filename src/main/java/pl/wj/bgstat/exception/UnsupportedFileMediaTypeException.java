package pl.wj.bgstat.exception;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import java.util.List;

public class UnsupportedFileMediaTypeException extends RuntimeException {
    public UnsupportedFileMediaTypeException(@Nullable MediaType mediaType, List<MediaType> supportedMediaTypes) {
        super(String.format("Unsupported %s media type. Supported media types: %s", mediaType, supportedMediaTypes.toString()));
    }
}
