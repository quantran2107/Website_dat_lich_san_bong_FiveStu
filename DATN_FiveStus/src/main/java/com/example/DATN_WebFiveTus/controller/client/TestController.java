package com.example.DATN_WebFiveTus.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("test-sidebar")
    public String testSidebar() {
        return "profile-customer";
    }
}
