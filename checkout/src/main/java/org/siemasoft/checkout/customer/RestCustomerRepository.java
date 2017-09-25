package org.siemasoft.checkout.customer;

import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestCustomerRepository implements CustomerRepository {

    @Value("${customer.service.url}")
    private String url;

    private final RestTemplate restTemplate;

    @Override
    public Customer findById(long customerId) {
        Single<Response> responseSingle = Single.fromCallable(() -> restTemplate.getForObject(url, Customer.class, customerId))
                                                .map(Response::success)
                                                .onErrorReturn(Response::error);
        Response<Customer> response = responseSingle.blockingGet();
        if (response.isError()) {
            throwDomainSpecificException(response);
        }
        return response.getDto();
    }

    private void throwDomainSpecificException(Response response) {
        Throwable throwable = response.getThrowable();
        if (response.hasErrorStatus(HttpStatus.NOT_FOUND)) {
            throw new CustomerNotFoundException(response.getErrorResponse());
        }
        throw new ServiceException(throwable, response.getErrorResponse());
    }
}