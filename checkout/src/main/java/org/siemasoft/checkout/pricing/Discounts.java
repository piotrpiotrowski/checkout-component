package org.siemasoft.checkout.pricing;

import java.util.List;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.siemasoft.checkout.card.item.Item;

@Value
@NonFinal
class Discounts {

    private final List<Item> items;

}
