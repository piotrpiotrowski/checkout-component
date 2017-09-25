package org.siemasoft.checkout.card

import org.siemasoft.checkout.card.item.Item
import org.siemasoft.checkout.card.repository.CardRepository
import org.siemasoft.checkout.customer.Customer
import org.siemasoft.checkout.dto.OrderDto
import spock.lang.Specification

class CardTest extends Specification {

    def "should return true when card belongs to customer"() {
        given:
        Customer owner = new Customer(1, "John")
        Card card = Card.builder()
                .customer(owner)
                .build()

        expect:
        card.isBelongingToCustomer(owner.id) == true
    }

    def "should return false when card does not belong to customer"() {
        given:
        Customer owner = new Customer(1, "John")
        Customer friend = new Customer(2, "Paul")
        Card card = Card.builder()
                .customer(owner)
                .build()

        expect:
        card.isBelongingToCustomer(friend.id) == false
    }

    def "should persist itself"() {
        given:
        Item expectedItem0 = new Item(123L, 3, new BigDecimal("100"))
        Item expectedItem1 = new Item(321L, 2, new BigDecimal("10"))
        Customer customer = new Customer(123, "John")
        Card card = Card.builder()
                .id("111:3333")
                .items([expectedItem0, expectedItem1])
                .customer(customer)
                .build()
        CardRepository cardRepository = Mock(CardRepository)

        when:
        Card persistedCard = card.persist(cardRepository)

        then:
        persistedCard.id.startsWith(customer.id + ":")
        1 * cardRepository.store({ it.id.startsWith(customer.id + ":") })
    }

    def "should convert to order"() {
        given:
        BigDecimal expectedSummaryPrice = new BigDecimal("110")
        Item expectedItem0 = new Item(123L, 3, new BigDecimal("100"))
        Item expectedItem1 = new Item(321L, 2, new BigDecimal("10"))
        Card card = Card.builder()
                .id("111:3333")
                .items([expectedItem0, expectedItem1])
                .build()

        when:
        OrderDto order = card.asOrder()

        then:
        with(order) {
            orderId == card.id
            summaryPrice == expectedSummaryPrice
            items.size() == card.items.size()
            with(items[0]) {
                id == expectedItem0.id
                quantity == expectedItem0.quantity
                price == expectedItem0.price
            }
            with(items[1]) {
                id == expectedItem1.id
                quantity == expectedItem1.quantity
                price == expectedItem1.price
            }

        }
    }

    def "should update cart with new items"() {
        given:
        Item newItem = new Item(555L, 5, new BigDecimal("150"))

        Item item = new Item(123L, 2, new BigDecimal("10"))
        Customer expectedCustomer = new Customer(123L, "John")
        Card card = Card.builder()
                .id(expectedCustomer.id + ":123")
                .items([item])
                .customer(expectedCustomer)
                .build()

        CardRepository cardRepository = Mock(CardRepository)

        when:
        Card updatedCard = card.update([newItem], cardRepository)

        then:
        with(updatedCard) {
            items.size() == 2
            items.containsAll([item, newItem])
        }
        1 * cardRepository.store({ it.id == card.id }) >> { it }
    }

    def "should update cart with changed items"() {
        given:
        Item changedItem = new Item(123L, 5, new BigDecimal("150"))

        Item item = new Item(123L, 2, new BigDecimal("10"))
        Customer expectedCustomer = new Customer(123L, "John")
        Card card = Card.builder()
                .id(expectedCustomer.id + ":123")
                .items([item])
                .customer(expectedCustomer)
                .build()

        CardRepository cardRepository = Mock(CardRepository)

        when:
        Card updatedCard = card.update([changedItem], cardRepository)

        then:
        with(updatedCard) {
            items.size() == 1
            items.contains(changedItem)
        }
        1 * cardRepository.store({ it.id == card.id }) >> { it }
    }

    def "should update cart with new and changed items"() {
        given:
        Item changedItem = new Item(123L, 5, new BigDecimal("150"))
        Item newItem = new Item(555L, 5, new BigDecimal("150"))
        Item item0 = new Item(123L, 2, new BigDecimal("10"))
        Item item1 = new Item(321L, 1, new BigDecimal("5"))
        Customer expectedCustomer = new Customer(123L, "John")
        Card card = Card.builder()
                .id(expectedCustomer.id + ":123")
                .items([item0, item1])
                .customer(expectedCustomer)
                .build()

        CardRepository cardRepository = Mock(CardRepository)

        when:
        Card updatedCard = card.update([changedItem, newItem], cardRepository)

        then:
        with(updatedCard) {
            items.size() == 3
            items.containsAll([changedItem, newItem, item1])
        }
        1 * cardRepository.store({ it.id == card.id }) >> { it }
    }
}