package es.maetd.marketcreators.bff.external.exception;

public class BffServiceException extends RuntimeException {
    public BffServiceException(String message) {
        super(message);
    }

    public BffServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
