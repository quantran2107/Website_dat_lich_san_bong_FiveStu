package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.rest.PhieuGiamGiaRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
//@RequestMapping("/web/")
public class PhieuGiamGiaController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/web-phieu-giam-gia")
    public String hienThi(Model model) {
        model.addAttribute("listPGG", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/api/hien-thi",
                PhieuGiamGiaDTO[].class
        )));
        return "/list/quan-ly-phieu-giam-gia";
    }
}
