package org.siemasoft.checkout.card.item;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Item implements Serializable {

    private final long id;

    private final long quantity;

    private final BigDecimal price;
}