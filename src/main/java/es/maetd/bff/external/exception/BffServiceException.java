package es.maetd.bff.external.exception;

public class BffServiceException extends RuntimeException {
    public BffServiceException(String message) {
        super(message);
    }

    public BffServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
