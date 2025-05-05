package org.myprojects.simple_shop_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myprojects.simple_shop_app.mapper.CustomerMapper;
import org.myprojects.simple_shop_app.model.dto.CustomerDTO;
import org.myprojects.simple_shop_app.model.request.UpdateCustomerEmailRequest;
import org.myprojects.simple_shop_app.service.CustomerService;
import org.myprojects.simple_shop_app.utils.converters.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers") // GET http://localhost:8080/customers
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerDetails(@PathVariable Long customerId) {
        log.info("[CustomerController][getCustomerDetails][customerId={}] стартовал", customerId);
        var response = customerService.getCustomer(customerId);
        log.info("[CustomerController][getCustomerDetails][customerId={}] успешно отработал: {}",
                customerId, JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(
                CustomerMapper.INSTANCE.customerToCustomerDTO(response)
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerEmail
    ) {
        log.info("[CustomerController][getCustomers] стартовал с customerName={}, customerEmail={}",
                customerName, customerEmail);
        var response = customerService.getCustomers(
                Optional.ofNullable(customerName), Optional.ofNullable(customerEmail)
        );
        log.info("[CustomerController][getCustomers] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(CustomerMapper.INSTANCE.customersToDTOs(response));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        log.info("[CustomerController][createCustomer] стратовал: {}", JsonConverter.toJson(customer).orElse(""));
        var response = customerService.createCustomer(
                CustomerMapper.INSTANCE.customerDTOToCustomer(customer)
        );
        log.info("[CustomerController][createCustomer] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomerMapper.INSTANCE.customerToCustomerDTO(response));
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customer) {
        log.info("[CustomerController][updateCustomer] стратовал: {}", JsonConverter.toJson(customer).orElse(""));
        var response = customerService.updateCustomer(
                CustomerMapper.INSTANCE.customerDTOToCustomer(customer)
        );
        log.info("[CustomerController][updateCustomer] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(CustomerMapper.INSTANCE.customerToCustomerDTO(response));
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomerEmail(
            @PathVariable Long customerId, @RequestBody UpdateCustomerEmailRequest request) {
        log.info("[CustomerController][updateCustomerEmail][customerId={}] стратовал: {}", customerId, JsonConverter.toJson(request).orElse(""));
        var response = customerService.updateCustomerEmail(customerId, request.newEmail());
        log.info("[CustomerController][updateCustomerEmail][customerId={}] получен успешный ответ: {}",
                customerId, JsonConverter.toJson(response).orElse(""));

        return ResponseEntity.ok(CustomerMapper.INSTANCE.customerToCustomerDTO(response));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        log.info("[CustomerController][deleteCustomer][customerId={}] стратовал", customerId);
        customerService.deleteCustomer(customerId);
        log.info("[CustomerController][deleteCustomer][customerId={}] успешно отработал", customerId);

        return ResponseEntity.ok("Покупатель успешно удален!");
    }
}
