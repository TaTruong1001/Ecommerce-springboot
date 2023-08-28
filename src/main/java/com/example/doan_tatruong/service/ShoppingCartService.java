package com.example.doan_tatruong.service;

import com.example.doan_tatruong.dto.ProductDto;
import com.example.doan_tatruong.model.Product;
import com.example.doan_tatruong.model.ShoppingCart;
import com.example.doan_tatruong.model.User;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username);
    ShoppingCart updateCart(ProductDto productDto, int quantity, String username);

    ShoppingCart removeItemFromCart(ProductDto productDto, String username);
    void deleteCartById(Long id);
}
