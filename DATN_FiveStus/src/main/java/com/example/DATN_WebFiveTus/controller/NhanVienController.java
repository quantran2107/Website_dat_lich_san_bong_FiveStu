package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class NhanVienController {

    @GetMapping("/quan-ly-nhan-vien")
    public String hienThi() {
        return "/list/nhan-vien/quan-ly-nhan-vien";
    }

    @GetMapping("/test")
    public String test() {
        return "/list/nhan-vien/test";
    }
}



