package org.siemasoft.checkout.card;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.siemasoft.checkout.card.item.Item;
import org.siemasoft.checkout.card.repository.CardRepository;
import org.siemasoft.checkout.customer.Customer;
import org.siemasoft.checkout.dto.ItemDto;
import org.siemasoft.checkout.dto.OrderDto;

@Builder
@Value
@NonFinal
public class Card implements Serializable {

    private final String id;

    private final Customer customer;

    private final List<Item> items;

    Card persist(CardRepository cardRepository) {
        Card card = Card.builder()
                        .id(calculateId())
                        .items(items)
                        .customer(customer)
                        .build();
        cardRepository.store(card);
        return card;
    }

    private String calculateId() {
        return customer.getId() + ":" + System.nanoTime();
    }

    public boolean isBelongingToCustomer(long customerId) {
        return this.customer.getId() == customerId;
    }

    public OrderDto asOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(id);
        List<ItemDto> dtos = items.stream()
                                  .map(this::createItemDto)
                                  .collect(Collectors.toList());
        orderDto.setItems(dtos);
        BigDecimal summaryPrice = items.stream()
                                       .map(Item::getPrice)
                                       .reduce(BigDecimal::add)
                                       .orElse(null);
        orderDto.setSummaryPrice(summaryPrice);
        return orderDto;
    }

    private ItemDto createItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setPrice(item.getPrice());
        return itemDto;
    }

    public Card update(List<Item> items, CardRepository cardRepository) {
        Map<Long, Item> currentItems = this.items.stream()
                                                 .collect(Collectors.toMap(Item::getId, Function.identity()));
        items.forEach(item -> currentItems.compute(item.getId(), (id, currentItem) -> item));
        Card updatedCard = Card.builder()
                               .id(id)
                               .items(new ArrayList<>(currentItems.values()))
                               .customer(customer)
                               .build();
        cardRepository.store(updatedCard);
        return updatedCard;
    }

}
