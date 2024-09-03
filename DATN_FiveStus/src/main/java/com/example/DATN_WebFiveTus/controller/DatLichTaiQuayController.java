package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DatLichTaiQuayController {
    @GetMapping("/dat-lich-tai-quay")
    public String hienThi() {
        return "list/dat-lich-tai-quay";
    }
}
