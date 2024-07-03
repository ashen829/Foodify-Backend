package com.ashen.Foodify.repository;

import com.ashen.Foodify.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
