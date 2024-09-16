package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LichDatController {

    @GetMapping("/quan-ly-lich-dat")
    public String hienThi(){
        return "list/lich-dat";
    }
}
