package org.siemasoft.checkout.pricing;

import java.util.List;

import org.siemasoft.checkout.card.item.Item;
import org.siemasoft.checkout.card.item.NotAllItemsFoundException;
import org.siemasoft.checkout.dto.ItemDto;

public interface PricingRepository {

    List<Item> findByIds(List<ItemDto> id) throws NotAllItemsFoundException;
}
