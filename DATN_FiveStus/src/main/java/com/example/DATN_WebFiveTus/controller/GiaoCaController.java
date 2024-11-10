package com.example.DATN_WebFiveTus.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GiaoCaController {

    @GetMapping("/nhan-ca")
    public String nhanCa(HttpServletRequest request) {
        return "";
    }

}
