package org.myprojects.simple_shop_app.service;

import org.myprojects.simple_shop_app.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    Customer updateCustomerEmail(Long customerId, String newEmail);

    Customer getCustomer(Long customerId);

    void deleteCustomer(Long customerId);

    List<Customer> getCustomers(Optional<String> customerName, Optional<String> customerEmail);
}
