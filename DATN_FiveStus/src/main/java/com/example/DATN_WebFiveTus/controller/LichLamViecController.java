package com.example.DATN_WebFiveTus.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class LichLamViecController {

    @GetMapping("lich-lam-viec")
    public String lichLamViec() {
        return "list/nhan-vien/lich-lam-viec";
    }


}
