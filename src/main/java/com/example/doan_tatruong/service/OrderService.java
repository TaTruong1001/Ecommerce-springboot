package com.example.doan_tatruong.service;

import com.example.doan_tatruong.model.Order;
import com.example.doan_tatruong.model.ShoppingCart;
import com.example.doan_tatruong.model.User;

import java.util.List;

public interface OrderService {
    Order saveOrder(ShoppingCart shoppingCart);

    List<Order> findALlOrders();

    List<Order> findAll(String username);

    Order acceptOrder(Long id);

    void cancelOrder(Long id);

    void sendOrderConfirmationEmail(User user);
    Order updatePaymentMethod(Order order);
}
