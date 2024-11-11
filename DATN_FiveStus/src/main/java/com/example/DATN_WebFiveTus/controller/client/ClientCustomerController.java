package com.example.DATN_WebFiveTus.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientCustomerController {

    @GetMapping("customer")
    public String customerProfile(){
        return "client/profile-customer";
    }

    @GetMapping("history-book")
    public String historyBook(){return "client/history-book";}
}
