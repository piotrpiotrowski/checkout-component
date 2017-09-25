package org.siemasoft.checkout.card.item;

import java.util.List;

import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.ErrorDetail;
import org.siemasoft.checkout.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public class InvalidItemsException extends ServiceException {

    public InvalidItemsException(List<Long> invalidItemsIds) {
        super(ErrorResponse.builder()
                           .message("Item quantity has to be greater than 0")
                           .code("invalid-items-list")
                           .status(HttpStatus.BAD_REQUEST.value())
                           .detail(new ErrorDetail("negative-number", "invalidItemsIds", invalidItemsIds))
                           .build());
    }
}