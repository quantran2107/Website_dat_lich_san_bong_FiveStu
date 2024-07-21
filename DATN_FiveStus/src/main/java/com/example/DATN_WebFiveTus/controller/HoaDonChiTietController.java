package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HoaDonChiTietController {

    @GetMapping("/web-hoa-don-chi-tiet")
    public String hienThi() {
        return "list/hoa-don-chi-tiet";
    }

}
