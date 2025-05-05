package org.myprojects.simple_shop_app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.exception.CustomerNotFoundException;
import org.myprojects.simple_shop_app.exception.ProductNotFoundException;
import org.myprojects.simple_shop_app.model.Customer;
import org.myprojects.simple_shop_app.model.Product;
import org.myprojects.simple_shop_app.repository.CustomerRepository;
import org.myprojects.simple_shop_app.service.CustomerService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        try {
            log.info("[CustomerServiceImpl][createCustomer] стартовал: {}", JsonConverter.toJson(customer).orElse(""));
            var response = customerRepository.save(customer);
            log.info("[CustomerServiceImpl][createCustomer] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[CustomerServiceImpl][createCustomer] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при создании покупателя!");
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        try {
            log.info("[CustomerServiceImpl][updateCustomer] стартовал: {}", JsonConverter.toJson(customer).orElse(""));
            var response = customerRepository.save(customer);
            log.info("[CustomerServiceImpl][updateCustomer] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[CustomerServiceImpl][updateCustomer] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении покупателя!");
        }
    }

    @Override
    public Customer updateCustomerEmail(Long customerId, String newEmail) {
        try {
            log.info("[CustomerServiceImpl][updateCustomerEmail][productId={}] стартовал с newEmail={}", customerId, newEmail);
            var response = customerRepository.findById(customerId)
                    .map(customer -> customerRepository.save(
                            customer.toBuilder()
                                    .email(newEmail)
                                    .build()
                    ))
                    .orElseThrow(
                            () -> new ProductNotFoundException("Покупатель не найден!")
                    );
            log.info("[CustomerServiceImpl][updateCustomerEmail][productId={}] успешно отработал: {}", customerId, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[CustomerServiceImpl][updateCustomerEmail][productId={}] получена ошибка: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении почты покупателя!");
        }
    }

    @Override
    public Customer getCustomer(Long customerId) {
        try {
            log.info("[CustomerServiceImpl][getCustomer][customerId={}] стратовал", customerId);
            var response = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("Продукт не найден!"));

            log.info("[CustomerServiceImpl][getCustomer][customerId={}] успешно отработал: {}",
                    customerId, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[CustomerServiceImpl][getCustomer][customerId={}] получена ошибка: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении деталей покупателя!");
        }
    }

    @Override
    public void deleteCustomer(Long customerId) {
        try {
            log.info("[CustomerServiceImpl][deleteCustomer][productId={}] стратовал", customerId);
            customerRepository.deleteById(customerId);

            log.info("[CustomerServiceImpl][deleteCustomer][productId={}] успешно отработал", customerId);
        } catch (Exception e) {
            log.error("[CustomerServiceImpl][deleteCustomer][productId={}] получена ошибка: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении покупателя!");
        }
    }

    @Override
    public List<Customer> getCustomers(Optional<String> customerName, Optional<String> customerEmail) {
        try {
            log.info("[CustomerServiceImpl][getCustomers] стратовал с customerName={}, customerEmail={}",
                    customerName, customerEmail);
            List<Customer> response = new ArrayList<>();

            if (customerName.isPresent() && customerEmail.isPresent())
                response = customerRepository.findByEmailAndFullNameContaining(customerEmail.get(), customerName.get());
            else if (customerName.isPresent())
                response = customerRepository.findByFullNameContainingIgnoreCase(customerName.get());
            else if (customerEmail.isPresent())
                response = customerRepository.findByEmail(customerEmail.get());
            else
                response = customerRepository.findAll();

            log.info("[CustomerServiceImpl][getCustomers] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[CustomerServiceImpl][getCustomers] получена ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка покупателей!");
        }
    }
}
