package org.myprojects.simple_shop_app.repository;

import org.myprojects.simple_shop_app.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByFullNameContainingIgnoreCase(String name);

    List<Customer> findByEmail(String email);

    List<Customer> findByEmailAndFullNameContaining(String email, String name);
}
