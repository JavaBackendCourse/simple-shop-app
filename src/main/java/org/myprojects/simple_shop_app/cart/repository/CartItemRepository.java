package org.myprojects.simple_shop_app.cart.repository;

import org.myprojects.simple_shop_app.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
