package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chon-san")
public class ChonSanController {
    @GetMapping("")
    public String hienThi(){
        return "/list/chon-san";
    }
}
