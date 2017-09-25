package org.siemasoft.checkout.customer;

import java.io.Serializable;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Customer implements Serializable {

    private final long id;

    private final String name;
}
