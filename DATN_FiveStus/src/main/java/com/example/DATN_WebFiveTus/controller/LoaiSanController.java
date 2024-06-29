package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller

public class LoaiSanController {

 private RestTemplate restTemplate;

 @Autowired
    public LoaiSanController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listLoaiSan")
    public String home(Model model) {
        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/loai-san/hien-thi",
                LoaiSanDTO[].class
        )));
        return "/list/quan-ly-san-bong";
    }
}
