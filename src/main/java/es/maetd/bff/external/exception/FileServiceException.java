package es.maetd.bff.external.exception;

public class FileServiceException extends BffServiceException {
    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
