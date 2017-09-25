package org.siemasoft.checkout.error;

import org.siemasoft.checkout.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = RestController.class, value = "org.siemasoft.checkout")
public class GlobalServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException exception, WebRequest request) {
        ErrorResponse errorResponse = exception.getErrorResponse();
        HttpStatus resolveHttpStatus = errorResponse.resolveHttpStatus();
        return new ResponseEntity<>(errorResponse, resolveHttpStatus);
    }

}
