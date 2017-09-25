package org.siemasoft.checkout.pricing

import org.siemasoft.checkout.card.item.Item
import org.siemasoft.checkout.card.item.NotAllItemsFoundException
import org.siemasoft.checkout.dto.ItemDto
import org.siemasoft.checkout.error.ServiceException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RestPricingRepositoryTest extends Specification {

    String url = "http://localhost:9920/items/discounts"

    RestTemplate restTemplate = Mock(RestTemplate)

    PricingRepository pricingRepository = new RestPricingRepository(restTemplate)

    def setup() {
        pricingRepository.url = url
    }

    def "should get items discounts by ids"() {
        given:
        List<Long> itemsDtos = [new ItemDto(quantity: 1, id: 12L), new ItemDto(quantity: 1, id: 32)]

        Discounts discounts = Mock(Discounts)
        discounts.items >> [Mock(Item), Mock(Item)]

        ResponseEntity responseEntity = Mock(ResponseEntity)
        responseEntity.getBody() >> discounts
        1 * restTemplate.postForEntity(url, _ as HttpEntity, Discounts) >> responseEntity

        when:
        List<Item> ids = pricingRepository.findByIds(itemsDtos)

        then:
        ids == discounts.getItems()
    }

    def "should throws NotAllItemsFoundException when not all item ids are existing"() {
        given:
        String errorResponseAsString = """
        {
            "message": "Not all items found",
            "code": "NOT_FOUND",
            "details":[
                {
                 "code": "not-exist",
                 "field": "ids",
                 "value": [32]
                }
            ]
        }
        """
        long nonFoundItemId = 32L
        List<Long> itemsDtos = [new ItemDto(quantity: 1, id: 12L), new ItemDto(quantity: 1, id: nonFoundItemId)]

        RestClientResponseException expectedException = new RestClientResponseException("not found", 404, null, null, errorResponseAsString.toCharArray() as byte[], null)
        1 * restTemplate.postForEntity(url, _ as HttpEntity, Discounts) >> {
            throw expectedException
        }

        when:
        pricingRepository.findByIds(itemsDtos)

        then:
        def exception = thrown(NotAllItemsFoundException)
        with(exception.errorResponse.details[0]) {
            field == "ids"
            value == [nonFoundItemId]
        }
    }

    def "should throws ServiceException unexpected exceptions"() {
        given:
        List<Long> itemsDtos = [new ItemDto(quantity: 1, id: 12L), new ItemDto(quantity: 1, id: 32)]

        RestClientException expectedException = new RestClientException("something went wrong")
        1 * restTemplate.postForEntity(url, _ as HttpEntity, Discounts) >> { throw expectedException }

        when:
        pricingRepository.findByIds(itemsDtos)

        then:
        def exception = thrown(ServiceException)
        exception.cause == expectedException
    }

    def "should throws ServiceException when HttpClientErrorException occures"() {
        given:
        List<Long> itemsDtos = [new ItemDto(quantity: 1, id: 12L), new ItemDto(quantity: 1, id: 32)]

        RestClientException expectedException = new HttpClientErrorException(HttpStatus.BAD_REQUEST)
        1 * restTemplate.postForEntity(url, _ as HttpEntity, Discounts) >> { throw expectedException }

        when:
        pricingRepository.findByIds(itemsDtos)

        then:
        def exception = thrown(ServiceException)
        exception.cause == expectedException
    }
}
