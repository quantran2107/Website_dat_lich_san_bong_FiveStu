package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.rest.HoaDonRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HoaDonController {

    @Autowired
    private HoaDonRest hoaDonRest;

    @GetMapping("/web-hoa-don")
    public String hienThi(Model model){
        model.addAttribute("listHD",hoaDonRest.getAll().getBody());
        return "/list/quan-ly-hoa-don";
    }
}
