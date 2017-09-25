package org.siemasoft.checkout.customer

import org.siemasoft.checkout.error.ServiceException
import org.siemasoft.checkout.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RestCustomerRepositoryTest extends Specification {

    String url = "http://localhost:9910/customers/{customerId}"

    RestTemplate restTemplate = Mock(RestTemplate)

    CustomerRepository customerRepository = new RestCustomerRepository(restTemplate)

    def setup() {
        customerRepository.url = url
    }

    def "should get customer by id"() {
        given:
        long customerId = 123L


        when:
        Customer customer = customerRepository.findById(customerId)

        then:
        customer != null
        1 * restTemplate.getForObject(url, Customer.class, customerId) >> Mock(Customer)
    }

    def "should throws CustomerNotFoundException when customer does not exists"() {
        given:
        long customerId = 123L
        String errorResponseAsString = """
        {
        "message": "Not found customer with id 0",
        "code": "NOT_FOUND",
        "status": 404,
        "details":[
            {
             "code": "not-exist",
             "field": "customerId",
             "value": $customerId
            }
        ]
       }
        """
        RestClientResponseException expectedException = new RestClientResponseException("not found", 404, null, null, errorResponseAsString.toCharArray() as byte[], null)
        1 * restTemplate.getForObject(url, Customer.class, customerId) >> {
            throw expectedException
        }

        when:
        customerRepository.findById(customerId)

        then:
        def exception = thrown(CustomerNotFoundException)
        ErrorResponse response = exception.errorResponse
        response.status == HttpStatus.NOT_FOUND.value()
        with(response.details[0]) {
            field == "customerId"
            value == customerId
        }
    }

    def "should throws ServiceException unexpected exceptions"() {
        given:
        long customerId = 123L
        RestClientException expectedException = new RestClientException("something went wrong")
        1 * restTemplate.getForObject(url, Customer.class, customerId) >> { throw expectedException }

        when:
        customerRepository.findById(customerId)

        then:
        def exception = thrown(ServiceException)
        exception.cause == expectedException
    }

    def "should throws ServiceException when HttpClientErrorException occures"() {
        given:
        long customerId = 123L
        RestClientException expectedException = new HttpClientErrorException(HttpStatus.BAD_REQUEST)
        1 * restTemplate.getForObject(url, Customer.class, customerId) >> { throw expectedException }

        when:
        customerRepository.findById(customerId)

        then:
        def exception = thrown(ServiceException)
        exception.cause == expectedException
    }
}