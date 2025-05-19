package org.myprojects.simple_shop_app.product.repository;

import org.myprojects.simple_shop_app.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);

    List<Product> findByCategory(String category);

    List<Product> findByCategoryAndName(String category, String name);
}
