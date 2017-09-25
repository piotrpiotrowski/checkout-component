package org.siemasoft.checkout.card.repository;

import org.siemasoft.checkout.error.ServiceException;

public class UnableToStoreCardException extends ServiceException {

    public UnableToStoreCardException(Exception errorResponse) {
        super(errorResponse);
    }
}
