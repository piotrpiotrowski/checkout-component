package org.siemasoft.checkout.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {

    private String orderId;

    private List<ItemDto> items;

    private BigDecimal summaryPrice;
}
