package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginPages")
    public String logins(){
        return "/login";
    }


}
