package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Link {

    @GetMapping("listThamSo")
    public String logins(){
        return "list/quan-ly-tham-so";
    }

}
