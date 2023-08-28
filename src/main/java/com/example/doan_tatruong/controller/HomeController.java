package com.example.doan_tatruong.controller;

import com.example.doan_tatruong.service.CategoryService;
import com.example.doan_tatruong.service.ProductService;
import com.example.doan_tatruong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
//    @RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
//    public String home(Principal principal, HttpSession session) {
//        if (principal != null) {
//            session.setAttribute("username", principal.getName());
//            User user = userService.findByUsername(principal.getName());
//            ShoppingCart shoppingCart = user.getShoppingCart();
//            session.setAttribute("totalItems", shoppingCart != null ? shoppingCart.getTotalItem() : 0);
//        }else {
//            session.removeAttribute("username");
//        }
//        return "/home";
//    }
}
