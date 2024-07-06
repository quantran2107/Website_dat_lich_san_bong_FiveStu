package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HoaDonController {

    @GetMapping("/web-hoa-don")
    public String hienThi() {
        return "list/quan-ly-hoa-don";
    }

}
