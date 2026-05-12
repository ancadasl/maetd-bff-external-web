package es.maetd.marketcreators.bff.external.exception;

import org.springframework.http.HttpStatus;

public class DocumentServiceException extends BffServiceException {

    private static final String ERROR_CODE = "DOCUMENT_UPLOAD_ERROR";
    private static final Integer HTTP_CODE = HttpStatus.BAD_REQUEST.value();

    public DocumentServiceException(String errorMessage) {
        super(errorMessage, ERROR_CODE, HTTP_CODE);
    }
}
