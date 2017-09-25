package org.siemasoft.checkout.sumary;

import lombok.Getter;
import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.ErrorDetail;
import org.siemasoft.checkout.response.ErrorResponse;
import org.springframework.http.HttpStatus;

@Getter
public class CardAccessDeniedException extends ServiceException {

    public CardAccessDeniedException(String cardId, long customerId) {
        super(ErrorResponse.builder()
                           .code("card-access-denied")
                           .status(HttpStatus.UNAUTHORIZED.value())
                           .detail(new ErrorDetail(null, "cardId", cardId))
                           .detail(new ErrorDetail(null, "customerId", customerId))
                           .build());
    }
}
