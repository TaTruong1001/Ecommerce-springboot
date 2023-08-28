package com.example.doan_tatruong.repository;

import com.example.doan_tatruong.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Modifying
    @Query("delete from CartItem c where c.cart.id= ?1")
    void deleteByCartId(Long cartId);
}
