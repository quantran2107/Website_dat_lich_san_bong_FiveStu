package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DichVuController {
    @GetMapping("/dich-vu")
    public String hienThi() {
        return "list/quan-ly-dich-vu";
    }
}
