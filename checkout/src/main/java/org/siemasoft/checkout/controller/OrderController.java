package org.siemasoft.checkout.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.siemasoft.checkout.card.Card;
import org.siemasoft.checkout.card.CardService;
import org.siemasoft.checkout.dto.ItemDto;
import org.siemasoft.checkout.dto.OrderDto;
import org.siemasoft.checkout.sumary.SummaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private static final String CUSTOMER_ID = "customerId";

    private static final String ORDER_ID = "orderId";

    private static final String CUSTOMERS = "customers";

    private static final String ORDERS = "orders";

    private final CardService cardService;

    private final SummaryService summaryService;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = '/' + CUSTOMERS + "/{" + CUSTOMER_ID + "}/" + ORDERS, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto create(@PathVariable(CUSTOMER_ID) long customerId, @RequestBody OrderDto orderDto) {
        Card card = cardService.create(orderDto, customerId);
        return card.asOrder();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = '/' + CUSTOMERS + "/{" + CUSTOMER_ID + "}/" + ORDERS + "/{" + ORDER_ID + '}', method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addItems(@PathVariable(CUSTOMER_ID) long customerId, @PathVariable(ORDER_ID) String orderId, @RequestBody List<ItemDto> items) {
        cardService.addItems(customerId, orderId, items);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = '/' + CUSTOMERS + "/{" + CUSTOMER_ID + "}/" + ORDERS + "/{" + ORDER_ID + '}', method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OrderDto get(@PathVariable(CUSTOMER_ID) long customerId, @PathVariable(ORDER_ID) String orderId) {
        return summaryService.getCustomerOrder(customerId, orderId);
    }
}
