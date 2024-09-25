package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test-chon-san")
public class TestChonSanController {
    @GetMapping("")
    public String hienThi(){
        return "/list/test-chon-san";
    }
}
