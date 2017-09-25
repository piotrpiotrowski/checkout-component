package org.siemasoft.checkout.customer;

import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.ErrorResponse;

public class CustomerNotFoundException extends ServiceException {

    public CustomerNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
