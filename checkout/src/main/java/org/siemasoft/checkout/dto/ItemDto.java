package org.siemasoft.checkout.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemDto {

    private long id;

    private long quantity;

    private BigDecimal price;

}
