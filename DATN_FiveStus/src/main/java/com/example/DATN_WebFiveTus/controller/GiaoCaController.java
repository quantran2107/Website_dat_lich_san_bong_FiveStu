package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.service.GiaoCaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GiaoCaController {

    @Autowired
    private GiaoCaService giaoCaService;

    @GetMapping("/nhan-ca")
    public String nhanCa(HttpServletRequest request) {
        return giaoCaService.getLastRow(request) ? "redirect:/quan-ly-nhan-vien" : "/list/nhan-vien/nhan-ca";
    }
}
