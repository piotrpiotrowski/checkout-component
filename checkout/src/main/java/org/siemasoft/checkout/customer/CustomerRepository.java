package org.siemasoft.checkout.customer;

public interface CustomerRepository {

    Customer findById(long customerId) throws CustomerNotFoundException;
}
