package com.example.doan_tatruong.repository;

import com.example.doan_tatruong.model.Order;
import com.example.doan_tatruong.model.OrderDetail;
import com.example.doan_tatruong.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    OrderDetail findByOrderAndProduct(Order order, Product product);
}
