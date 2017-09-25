package org.siemasoft.checkout.response

import org.siemasoft.checkout.customer.Customer
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClientResponseException
import spock.lang.Specification
import spock.lang.Unroll

class ResponseTest extends Specification {

    def "should return false when exception does not contain http status"() {
        given:
        Response response = Response.error(new Exception())

        expect:
        response.hasErrorStatus(HttpStatus.BAD_REQUEST) == false
    }

    @Unroll
    def "should return #expected when RestClientResponseException has status: #status"() {
        given:
        Response response = Response.error(new RestClientResponseException("", status.value(), null, null, null, null))

        expect:
        response.hasErrorStatus(HttpStatus.BAD_REQUEST) == expected

        where:
        status                 | expected
        HttpStatus.BAD_REQUEST | true
        HttpStatus.NOT_FOUND   | false
    }

    def "should create response with error response"() {
        given:
        long nonFoundItemId = 32L
        String expectedCode = "NOT_FOUND"
        String errorResponseAsString = """
        {
            "message": "Not all items found",
            "code": "$expectedCode",
            "details":[
                {
                 "code": "not-exist",
                 "field": "ids",
                 "value": [$nonFoundItemId]
                }
            ]
        }
        """
        RestClientResponseException restException = new RestClientResponseException("not found", 404, null, null, errorResponseAsString.toCharArray() as byte[], null)

        when:
        Response response = Response.error(restException)

        then:
        response.errorResponse != null
        with(response.errorResponse) {
            code == expectedCode
            details[0].value == [nonFoundItemId]
        }
    }

    @Unroll
    def "should return #expected when #response"() {
        expect:
        response.isError() == expected

        where:
        response                              | expected
        Response.error(new Exception())       | true
        Response.success(new Customer(1, "")) | false
    }

    @Unroll
    def "should throw IllegalArgumentException with message: dto cannot be null"() {
        when:
        Response.success(null)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "dto cannot be null"
    }
}