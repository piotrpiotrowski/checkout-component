package org.siemasoft.checkout.error;

import lombok.Getter;
import org.siemasoft.checkout.response.ErrorResponse;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private ErrorResponse errorResponse;

    public ServiceException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
        this.errorResponse = ErrorResponse.builder()
                                          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                          .code("internal-service-error")
                                          .build();
    }

    public ServiceException(Throwable cause, ErrorResponse errorResponse) {
        super(cause);
        this.errorResponse = errorResponse;
    }
}