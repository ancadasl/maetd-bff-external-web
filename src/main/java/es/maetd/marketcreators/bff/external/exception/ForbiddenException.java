package es.maetd.marketcreators.bff.external.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends BffServiceException {
    public ForbiddenException(String message) {
        super(message);
    }
}
