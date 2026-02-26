package es.maetd.marketcreators.bff.external.exception;

import org.springframework.http.HttpStatus;

public class ReportTransferException extends BffServiceException {

    private static final String ERROR_CODE = "REPORT_ERROR";
    private static final Integer HTTP_CODE = HttpStatus.BAD_REQUEST.value();

    public ReportTransferException(String errorMessage) {
        super(errorMessage, ERROR_CODE, HTTP_CODE);
    }
}
