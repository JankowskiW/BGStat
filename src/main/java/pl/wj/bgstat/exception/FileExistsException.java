package pl.wj.bgstat.exception;

public class FileExistsException extends RuntimeException {
    public FileExistsException() {
        super("File exists and file creation attempt limit exceeded.");
    }
}
