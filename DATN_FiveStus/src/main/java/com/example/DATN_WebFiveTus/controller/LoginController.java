package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/dang-nhap")
    public String ht(){
        return "list/Login";
    }
    @GetMapping("/dang-ky")
    public String htDK(){
        return "list/dang-ky";
    }
    @GetMapping("/quen-mk")
    public String htQMK(){
        return "list/quen-mk";
    }
}
