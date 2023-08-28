package com.example.doan_tatruong.service.impl;

import com.example.doan_tatruong.model.*;
import com.example.doan_tatruong.repository.OrderDetailRepository;
import com.example.doan_tatruong.repository.OrderRepository;
import com.example.doan_tatruong.repository.UserRepository;
import com.example.doan_tatruong.service.OrderService;
import com.example.doan_tatruong.service.ProductService;
import com.example.doan_tatruong.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductService productService;

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    @Transactional
    public Order saveOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setUser(shoppingCart.getUser());
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");
        order.setQuantity(shoppingCart.getTotalItem());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);

        for (CartItem item : shoppingCart.getCartItems()) {
            Product product = item.getProduct();
            int requestedQuantity = item.getQuantity();

            if (product.getCurrentQuantity() >= requestedQuantity) {
                product.setCurrentQuantity(product.getCurrentQuantity() - requestedQuantity);
            } else {
                throw new RuntimeException("Số lượng sản phẩm không đủ");
            }

            productService.saveProduct(product);

        }
        shoppingCartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAll(String username) {
        User user = userRepository.findByUsername(username);
        List<Order> orders = user.getOrders();
        return orders;
    }


    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }


    @Override
    public void sendOrderConfirmationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Order Confirmation");
        message.setText("Thank you for your order!");

        javaMailSender.send(message);
    }

    @Override
    public Order updatePaymentMethod(Order order) {
        if (order.getPaymentMethod().equals("PayPal")) {
            order.setPaymentMethod("Paid via PayPal");
        } else {
            order.setPaymentMethod("Cash");
        }
        return orderRepository.save(order);
    }
}