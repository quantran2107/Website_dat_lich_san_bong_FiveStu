package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class HoaDonController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/web-hoa-don")
    public String hienThi(Model model) {
        model.addAttribute("listHD", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/hoa-don/hien-thi",
                HoaDonDTO[].class
        )));
        return "/list/quan-ly-hoa-don";
    }
}
