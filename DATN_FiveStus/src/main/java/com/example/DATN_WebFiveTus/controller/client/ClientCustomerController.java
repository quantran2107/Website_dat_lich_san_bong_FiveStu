package com.example.DATN_WebFiveTus.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientCustomerController {
    @GetMapping("customer-details")
    public String testSidebar() {
        return "client/profile-customer";
    }
}