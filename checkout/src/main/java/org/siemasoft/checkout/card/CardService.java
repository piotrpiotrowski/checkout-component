package org.siemasoft.checkout.card;

import java.util.List;
import java.util.Optional;

import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.siemasoft.checkout.card.item.Item;
import org.siemasoft.checkout.card.item.Items;
import org.siemasoft.checkout.card.repository.CardRepository;
import org.siemasoft.checkout.customer.CustomerRepository;
import org.siemasoft.checkout.dto.ItemDto;
import org.siemasoft.checkout.dto.OrderDto;
import org.siemasoft.checkout.pricing.PricingRepository;
import org.siemasoft.checkout.sumary.CardAccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CustomerRepository customerRepository;

    private final PricingRepository pricingRepository;

    private final CardRepository cardRepository;

    public Card create(OrderDto orderDto, long customerId) {
        return Single.just(customerId)
                     .map(customerRepository::findById)
                     .map(customer -> Card.builder().customer(customer))
                     .map(cardBuilder -> addItemsToCardBuilder(cardBuilder, orderDto.getItems()))
                     .map(Card.CardBuilder::build)
                     .map(c -> c.persist(cardRepository))
                     .blockingGet();
    }

    private Card.CardBuilder addItemsToCardBuilder(Card.CardBuilder cardBuilder, List<ItemDto> itemsDtos) {
        Items items = Items.withPrices(itemsDtos, pricingRepository);
        return cardBuilder.items(items.getList());
    }

    public void addItems(long customerId, String cardId, List<ItemDto> itemsDtos) {
        Card updatedCard = Optional.ofNullable(cardRepository.findById(cardId))
                                   .filter(card -> card.isBelongingToCustomer(customerId))
                                   .map(card -> updateCard(card, itemsDtos))
                                   .orElseThrow(() -> new CardAccessDeniedException(cardId, customerId));
    }

    private Card updateCard(Card card, List<ItemDto> itemsDtos) {
        List<Item> items = pricingRepository.findByIds(itemsDtos);
        return card.update(items, cardRepository);
    }
}
