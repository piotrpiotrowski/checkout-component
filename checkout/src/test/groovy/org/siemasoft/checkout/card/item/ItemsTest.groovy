package org.siemasoft.checkout.card.item

import org.siemasoft.checkout.dto.ItemDto
import org.siemasoft.checkout.pricing.PricingRepository
import spock.lang.Specification
import spock.lang.Unroll

class ItemsTest extends Specification {

    PricingRepository pricingRepository = Mock(PricingRepository)

    @Unroll
    def "should throw EmptyItemsListException for #caseDescription"() {
        when:
        Items.withPrices(items, pricingRepository)

        then:
        thrown(EmptyItemsListException)

        where:
        items | caseDescription
        null  | "null list of items"
        []    | "empty list of items"
    }

    def "should throws InvalidItemsException if at least item has quantity smaller that 1"() {
        given:
        List<ItemDto> itemsDtos = [new ItemDto(id: 123L, quantity: -1),
                                   new ItemDto(id: 321L, quantity: 0),
                                   new ItemDto(id: 456, quantity: 1)]

        when:
        Items.withPrices(itemsDtos, pricingRepository)

        then:
        InvalidItemsException exception = thrown(InvalidItemsException)
        with(exception.errorResponse.details[0]) {
            field == "invalidItemsIds"
            value == [itemsDtos[0].id, itemsDtos[1].id]
        }
    }

    def "should create Items with prices"() {
        given:
        List<ItemDto> itemsDtos = [new ItemDto(id: 123L, quantity: 1), new ItemDto(id: 321L, quantity: 21)]

        Item expectedItem0 = new Item(itemsDtos[0].id, 3, new BigDecimal("100"))
        Item expectedItem1 = new Item(itemsDtos[1].id, 2, new BigDecimal("10"))
        List expectedItemsList = [expectedItem0, expectedItem1]
        pricingRepository.findByIds(itemsDtos) >> expectedItemsList

        when:
        Items items = Items.withPrices(itemsDtos, pricingRepository)

        then:
        items != null
        items.list == expectedItemsList
    }
}