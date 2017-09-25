package org.siemasoft.checkout.sumary

import org.siemasoft.checkout.card.Card
import org.siemasoft.checkout.card.repository.CardRepository
import org.siemasoft.checkout.dto.OrderDto
import spock.lang.Specification

class SummaryServiceTest extends Specification {

    CardRepository cardRepository = Mock(CardRepository)

    SummaryService summaryService = new SummaryService(cardRepository)

    def "should throw CardNotFoundException when card does not exist"() {
        given:
        String cardId = "3333:1111111111"
        long customerId = 11111

        cardRepository.findById(cardId) >> { throw new CardNotFoundException(cardId) }

        when:
        summaryService.getCustomerOrder(customerId, cardId)

        then:
        CardNotFoundException exception = thrown(CardNotFoundException)
        with(exception.errorResponse.details[0]) {
            field == "cardId"
            value == cardId
        }

    }

    def "should throw CardAccessDeniedException when card does not belong to customer"() {
        given:
        String cardId = "3333:1111111111"
        long customerId = 11111
        long otherCustomerId = 1

        Card card = Mock(Card)
        card.isBelongingToCustomer(otherCustomerId) >> false
        cardRepository.findById(cardId) >> card

        when:
        summaryService.getCustomerOrder(customerId, cardId)

        then:
        CardAccessDeniedException exception = thrown(CardAccessDeniedException)
        with(exception.errorResponse.details[0]) {
            field == "cardId"
            value == cardId
        }
        with(exception.errorResponse.details[1]) {
            field == "customerId"
            value == customerId
        }
    }

    def "should get summary"() {
        given:
        String cardId = "3333:1111111111"
        long customerId = 11111

        OrderDto expectedOrder = Mock(OrderDto)
        Card card = Mock(Card)
        card.isBelongingToCustomer(customerId) >> true
        card.asOrder() >> expectedOrder
        cardRepository.findById(cardId) >> card

        when:
        OrderDto orderDto = summaryService.getCustomerOrder(customerId, cardId)

        then:
        orderDto == expectedOrder
    }
}