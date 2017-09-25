package org.siemasoft.checkout.card.item;

import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public class EmptyItemsListException extends ServiceException {

    EmptyItemsListException() {
        super(ErrorResponse.builder()
                           .message("Order has to have at least one item")
                           .code("empty-items-list")
                           .status(HttpStatus.BAD_REQUEST.value())
                           .build());
    }
}