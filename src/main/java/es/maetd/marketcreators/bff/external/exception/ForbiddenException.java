package es.maetd.marketcreators.bff.external.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends BffServiceException {

    private static final String ERROR_CODE = "FORBIDDEN";
    private static final Integer HTTP_CODE = HttpStatus.BAD_REQUEST.value();

    public ForbiddenException(String errorMessage) {
        super(errorMessage, ERROR_CODE, HTTP_CODE);
    }
}
