package org.siemasoft.checkout.pricing;

import java.util.List;

import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.siemasoft.checkout.card.item.Item;
import org.siemasoft.checkout.card.item.NotAllItemsFoundException;
import org.siemasoft.checkout.dto.ItemDto;
import org.siemasoft.checkout.error.ServiceException;
import org.siemasoft.checkout.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestPricingRepository implements PricingRepository {

    @Value("${pricing.service.url}")
    private String url;

    private final RestTemplate restTemplate;

    @Override
    public List<Item> findByIds(List<ItemDto> itemsDtos) {
        Single<Response> responseSingle = Single.just(createHttpEntity(itemsDtos))
                                                .map(request -> restTemplate.postForEntity(url, request, Discounts.class))
                                                .map(HttpEntity::getBody)
                                                .map(Response::success)
                                                .onErrorReturn(Response::error);
        Response<Discounts> response = responseSingle.blockingGet();
        if (response.isError()) {
            throwDomainSpecificException(response);
        }
        return response.getDto().getItems();
    }

    private HttpEntity<List<ItemDto>> createHttpEntity(List<ItemDto> itemsDtos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(itemsDtos, headers);
    }

    private void throwDomainSpecificException(Response response) {
        if (response.hasErrorStatus(HttpStatus.NOT_FOUND)) {
            throw new NotAllItemsFoundException(response.getErrorResponse());
        }
        throw new ServiceException(response.getThrowable(), response.getErrorResponse());
    }
}