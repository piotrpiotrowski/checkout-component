package org.siemasoft.checkout.card.item;

import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import lombok.Value;
import org.apache.commons.collections4.CollectionUtils;
import org.siemasoft.checkout.dto.ItemDto;
import org.siemasoft.checkout.pricing.PricingRepository;


@Value
public class Items {

    private final List<Item> list;

    private Items(List<Item> list) {
        this.list = list;
    }

    public static Items withPrices(List<ItemDto> itemsDtos, PricingRepository pricingRepository) {
        List<ItemDto> items = Optional.ofNullable(itemsDtos)
                                      .filter(CollectionUtils::isNotEmpty)
                                      .orElseThrow(EmptyItemsListException::new);
        validateItems(items);
        List<Item> list = pricingRepository.findByIds(items);
        return new Items(list);
    }

    private static void validateItems(List<ItemDto> items) {
        Observable.fromIterable(items)
                  .filter(item -> item.getQuantity() <= 0)
                  .map(ItemDto::getId)
                  .toList()
                  .filter(CollectionUtils::isNotEmpty)
                  .doOnSuccess(ids -> {
                      throw new InvalidItemsException(ids);
                  })
                  .blockingGet();
    }
}