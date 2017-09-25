package org.siemasoft.checkout.sumary;

import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.ErrorDetail;
import org.siemasoft.checkout.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public class CardNotFoundException extends ServiceException {

    public CardNotFoundException(String cardId) {
        super(ErrorResponse.builder()
                           .code("card-not-found")
                           .status(HttpStatus.NOT_FOUND.value())
                           .detail(new ErrorDetail(null, "cardId", cardId))
                           .build());
    }
}
