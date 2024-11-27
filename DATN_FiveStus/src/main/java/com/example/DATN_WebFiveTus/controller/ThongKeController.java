package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("thong-ke")
public class ThongKeController {
    @GetMapping("")
    public String hienThi(){
        return "/list/thong-ke";
    }
}
