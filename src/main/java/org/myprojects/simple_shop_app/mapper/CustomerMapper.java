package org.myprojects.simple_shop_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.myprojects.simple_shop_app.model.Customer;
import org.myprojects.simple_shop_app.model.dto.CustomerDTO;

import java.util.List;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO customerToCustomerDTO(Customer customer);

    List<CustomerDTO> customersToDTOs(List<Customer> customer);

    Customer customerDTOToCustomer(CustomerDTO customer);
}
