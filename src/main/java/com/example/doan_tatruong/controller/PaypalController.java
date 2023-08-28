package com.example.doan_tatruong.controller;

import com.example.doan_tatruong.model.Order;
import com.example.doan_tatruong.model.ShoppingCart;
import com.example.doan_tatruong.model.User;
import com.example.doan_tatruong.service.OrderService;
import com.example.doan_tatruong.service.PaypalService;
import com.example.doan_tatruong.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class PaypalController {

    @Autowired
    private PaypalService paypalService;
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @PostMapping("/create-payment")
    @ResponseBody // Để trả về dữ liệu JSON
    public ResponseEntity<String> createPayment(
            @RequestParam("total") Double total,
            @RequestParam("currency") String currency) {
        try {
            String cancelUrl = "http://localhost:8080/cancel-payment";
            String successUrl = "http://localhost:8080/success-payment";


            Payment payment = paypalService.createPayment(
                    total, currency, "paypal", "sale", "Payment description", cancelUrl, successUrl);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok().body(link.getHref()); // Trả về URL chấp thuận
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    // Helper method to get the base URL of the application
    private String getBaseUrl(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

    @GetMapping("/cancel-payment")
    public String cancelPayment() {

        return "checkout";
    }

    @GetMapping("/success-payment")
    public String successPayment(
            Principal principal,
            Model model) {

            // Lưu đơn hàng và gửi email xác nhận
            User user = userService.findByUsername(principal.getName());
            ShoppingCart shoppingCart = user.getShoppingCart();
            Order newOrder = orderService.saveOrder(shoppingCart);
            model.addAttribute("orders", newOrder);
            orderService.sendOrderConfirmationEmail(user);

            // Xác định phương thức thanh toán và cập nhật vào đơn hàng
            if (newOrder.getPaymentMethod().equals("PayPal")) {
                newOrder.setPaymentMethod("Paid via PayPal");
            } else {
                newOrder.setPaymentMethod("Cash");
            }

            // Truyền thông tin thanh toán vào model để hiển thị
            model.addAttribute("orderConfirmationMessage", "Your order has been successfully placed. An email confirmation has been sent to your email address.");
        return "order";
    }


    // Class đơn giản để trả về URL từ controller cho JavaScript
    private static class PaymentResponse {
        private String approvalUrl;

        public PaymentResponse(String approvalUrl) {
            this.approvalUrl = approvalUrl;
        }

        public String getApprovalUrl() {
            return approvalUrl;
        }
    }
}
