package org.siemasoft.checkout.card

import org.siemasoft.checkout.card.item.Item
import org.siemasoft.checkout.card.item.NotAllItemsFoundException
import org.siemasoft.checkout.card.repository.CardRepository
import org.siemasoft.checkout.card.repository.UnableToStoreCardException
import org.siemasoft.checkout.customer.Customer
import org.siemasoft.checkout.customer.CustomerNotFoundException
import org.siemasoft.checkout.customer.CustomerRepository
import org.siemasoft.checkout.dto.ItemDto
import org.siemasoft.checkout.dto.OrderDto
import org.siemasoft.checkout.pricing.PricingRepository
import org.siemasoft.checkout.response.ErrorResponse
import org.siemasoft.checkout.sumary.CardAccessDeniedException
import org.siemasoft.checkout.sumary.CardNotFoundException
import spock.lang.Specification

class CardServiceTest extends Specification {

    CustomerRepository customerRepository = Mock(CustomerRepository)

    PricingRepository pricingRepository = Mock(PricingRepository)

    CardRepository cardRepository = Mock(CardRepository)

    CardService service = new CardService(customerRepository, pricingRepository, cardRepository)

    def "should throw CustomerNotFoundException when customer does not exist"() {
        given:
        long customerId = -1

        OrderDto orderDto = Mock(OrderDto)
        orderDto.getItems() >> [new ItemDto(quantity: 1, id: 12)]

        ErrorResponse errorResponse = Mock(ErrorResponse)
        customerRepository.findById(customerId) >> {
            throw new CustomerNotFoundException(errorResponse)
        }

        when:
        service.create(orderDto, customerId)

        then:
        CustomerNotFoundException exception = thrown(CustomerNotFoundException)
        exception.errorResponse == errorResponse
    }

    def "should throw NotAllItemsFoundException when at least item was not found"() {
        given:
        long customerId = 123
        long nonFoundItemId = 32
        List<Long> itemsDtos = [new ItemDto(quantity: 1, id: 12L), new ItemDto(quantity: 1, id: nonFoundItemId)]

        OrderDto orderDto = Mock(OrderDto)
        orderDto.getItems() >> itemsDtos

        Customer expectedCustomer = new Customer(customerId, "John")
        customerRepository.findById(customerId) >> expectedCustomer

        ErrorResponse errorResponse = Mock(ErrorResponse)
        pricingRepository.findByIds(itemsDtos) >> {
            throw new NotAllItemsFoundException(errorResponse)
        }

        when:
        service.create(orderDto, customerId)

        then:
        NotAllItemsFoundException exception = thrown(NotAllItemsFoundException)
        exception.errorResponse == errorResponse
    }

    def "should throw UnableToStoreCardException when storing order failed"() {
        given:
        long customerId = 321
        List<ItemDto> itemsDtos = [new ItemDto(quantity: 3, id: 123L)]

        OrderDto orderDto = Mock(OrderDto)
        orderDto.getItems() >> itemsDtos

        Customer expectedCustomer = new Customer(customerId, "John")
        customerRepository.findById(customerId) >> expectedCustomer

        Item expectedItem = new Item(itemsDtos[0].id, 3, new BigDecimal("100"))
        pricingRepository.findByIds(itemsDtos) >> [expectedItem]

        cardRepository.store(_ as Card) >> { throw new UnableToStoreCardException() }

        when:
        service.create(orderDto, customerId)

        then:
        thrown(UnableToStoreCardException)
    }

    def "should  create order with single item"() {
        given:
        long customerId = 321
        List<ItemDto> itemsDtos = [new ItemDto(quantity: 2, id: 123L)]

        OrderDto orderDto = Mock(OrderDto)
        orderDto.getItems() >> itemsDtos

        Customer expectedCustomer = new Customer(customerId, "John")
        customerRepository.findById(customerId) >> expectedCustomer

        Item expectedItem = new Item(itemsDtos[0].id, 2, new BigDecimal("100"))
        pricingRepository.findByIds(itemsDtos) >> [expectedItem]

        when:
        Card card = service.create(orderDto, customerId)

        then:
        card.id.startsWith(customerId + ":")

        1 * cardRepository.store({
            with(it) {
                it.customer == expectedCustomer
                items.size() == 1
                items[0].price == expectedItem.price
            }
            return it
        })
    }

    def "should  create order with multiple items"() {
        given:
        long customerId = 321
        List<ItemDto> itmesDtos = [new ItemDto(quantity: 3, id: 123L), new ItemDto(quantity: 2, id: 321L)]

        OrderDto orderDto = Mock(OrderDto)
        orderDto.getItems() >> itmesDtos

        Customer expectedCustomer = new Customer(customerId, "John")
        customerRepository.findById(customerId) >> expectedCustomer

        Item expectedItem0 = new Item(itmesDtos[0].id, 3, new BigDecimal("100"))
        Item expectedItem1 = new Item(itmesDtos[1].id, 2, new BigDecimal("10"))
        pricingRepository.findByIds(itmesDtos) >> [expectedItem0, expectedItem1]

        when:
        Card card = service.create(orderDto, customerId)

        then:
        card.id.startsWith(customerId + ":")
        1 * cardRepository.store({
            with(it) {
                it.customer == expectedCustomer
                items.size() == 2
                items[0].price == expectedItem0.price
                items[1].price == expectedItem1.price
            }
            return it
        })
    }

    def "should throw CardNotFoundException when card does not exist"() {
        given:
        String cardId = "3333:1111111111"
        long customerId = 11111
        List<ItemDto> items = []

        cardRepository.findById(cardId) >> { throw new CardNotFoundException(cardId) }

        when:
        service.addItems(customerId, cardId, items)

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
        List<ItemDto> items = []

        Card card = Mock(Card)
        card.isBelongingToCustomer(otherCustomerId) >> false
        cardRepository.findById(cardId) >> card

        when:
        service.addItems(customerId, cardId, items)

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

    def "should update cart with new and changed items"() {
        given:
        String cardId = "3333:1111111111"
        long customerId = 11111
        List<ItemDto> itemsDtos = [new ItemDto(quantity: 3, id: 555L), new ItemDto(quantity: 2, id: 321L)]

        Card card = Mock(Card)
        card.isBelongingToCustomer(customerId) >> true
        cardRepository.findById(cardId) >> card

        Item newItem = new Item(itemsDtos[0].id, 5, new BigDecimal("150"))
        Item changedItem = new Item(itemsDtos[1].id, 2, new BigDecimal("10"))
        List<Item> expectedItems = [newItem, changedItem]
        pricingRepository.findByIds(itemsDtos) >> expectedItems

        when:
        service.addItems(customerId, cardId, itemsDtos)

        then:
        1 * card.update(expectedItems, cardRepository) >> Mock(Card)
    }

}