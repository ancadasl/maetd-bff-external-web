package es.maetd.marketcreators.bff.external.exception;

import es.maetd.marketcreators.ddd.core.errors.DomainError;

public class BffServiceException extends DomainError {

    public BffServiceException(String message) {
        super(message);
    }

    public BffServiceException(String errorMessage, String errorCode, Integer httpCode) {
        super(errorMessage, errorCode, httpCode);
    }
}
