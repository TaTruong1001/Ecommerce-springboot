package com.example.doan_tatruong.controller;

import com.example.doan_tatruong.dto.ProductDto;
import com.example.doan_tatruong.model.ShoppingCart;
import com.example.doan_tatruong.model.User;
import com.example.doan_tatruong.service.ProductService;
import com.example.doan_tatruong.service.ShoppingCartService;
import com.example.doan_tatruong.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    UserService userService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    ProductService productService;

    @RequestMapping(value = "customer/cart", method = RequestMethod.GET)
    public String cart(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            ShoppingCart shoppingCart = user.getShoppingCart();
            if (shoppingCart == null) {
                model.addAttribute("msg", "no item in your cart");
                model.addAttribute("subTotal",0);
            }else{
                double subTotal = shoppingCart.getTotalPrice();
                System.out.println(shoppingCart.getTotalPrice());
                model.addAttribute("subTotal", subTotal);
                model.addAttribute("shoppingCart", shoppingCart);
            }
            return "cart";
        }
    }

    @RequestMapping(value = "/add-to-cart",method = RequestMethod.POST)
    public String addItemToCart(@RequestParam("id") Long productId,
                                @RequestParam(value = "quantity", required = true, defaultValue = "1") int quantity,
                                Principal principal,
                                HttpServletRequest request, HttpSession session, Model model){
        ProductDto productDto = productService.getById(productId);
       if (principal == null){
           return "redirect:/login";
       }else {
           String username = principal.getName();
           User user = userService.findByUsername(username);
           ShoppingCart shoppingCart = shoppingCartService.addItemToCart(productDto, quantity, username);
           session.setAttribute("totalItems", shoppingCart.getTotalItem());
           model.addAttribute("shoppingCart", shoppingCart);
       }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart",method = RequestMethod.POST,params = "action=update")
    public String updateCart(@RequestParam(value = "quantity") int quantity,
                             @RequestParam("id") Long productId, Model model, Principal principal, HttpSession session){
        if (principal == null) {
            return "redirect:/login";
        } else {
            ProductDto productDto = productService.getById(productId);
            String username = principal.getName();
            User user = userService.findByUsername(username);
            ShoppingCart shoppingCart = shoppingCartService.updateCart(productDto, quantity, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/customer/cart";
        }

    }
    @RequestMapping(value = "/update-cart",method = RequestMethod.POST,params = "action=delete")
    public String deleteCart(@RequestParam("id") Long productId, Model model, Principal principal,HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }else {
            ProductDto productDto = productService.getById(productId);
            String username = principal.getName();
            User user = userService.findByUsername(username);
            ShoppingCart shoppingCart = shoppingCartService.removeItemFromCart(productDto, username);

            session.setAttribute("totalItems", shoppingCart.getTotalItem());
            model.addAttribute("shoppingCart", shoppingCart);
        }
        return "redirect:/customer/cart";
    }
}
