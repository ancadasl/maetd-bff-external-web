package es.maetd.bff.external.exception;

public class DocumentServiceException extends BffServiceException {
    public DocumentServiceException(String message) {
        super(message);
    }

    public DocumentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
