package org.siemasoft.checkout.card.item;

import lombok.Getter;
import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.ErrorResponse;

@Getter
public class NotAllItemsFoundException extends ServiceException {

    public NotAllItemsFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
