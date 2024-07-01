package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class GiaoCaController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GiaoCaService giaoCaService;

    @GetMapping("/quan-ly-giao-ca")
    public String hienThiGC(Model model) {
        model.addAttribute("listGC",
                Arrays.asList(restTemplate.getForObject(
                        "http://localhost:8080/giao-ca/hien-thi", GiaoCaDTO[].class
                )));
        return "/list/nhan-vien/lich-su-giao-ca";
    }

    @PostMapping("/giao-ca")
    public String giaoCa(@ModelAttribute GiaoCaDTO giaoCaDTO) {
        giaoCaService.save(giaoCaDTO);
        return "redirect:/quan-ly-giao-ca";
    }
}
