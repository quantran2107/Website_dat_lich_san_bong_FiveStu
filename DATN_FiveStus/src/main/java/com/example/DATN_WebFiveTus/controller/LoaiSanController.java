package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.service.LoaiSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller

public class LoaiSanController {

    private LoaiSanService loaiSanService;

    private RestTemplate restTemplate;

    @Autowired
    public LoaiSanController(LoaiSanService loaiSanService, RestTemplate restTemplate) {
        this.loaiSanService = loaiSanService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listLoaiSan")
    public String HienThi(Model model) {
        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/loai-san/hien-thi",
                LoaiSanDTO[].class
        )));
        model.addAttribute("loaiSan",new LoaiSanDTO());
        return "/list/quan-ly-san-bong";
    }

    @PostMapping("/loaiSan/add")
    public String add(@ModelAttribute("loaiSan") LoaiSanDTO loaiSanDTO){
        loaiSanService.save(loaiSanDTO);
        return "redirect:/listLoaiSan";
    }

    @GetMapping("/loaiSan/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        LoaiSanDTO loaiSan = restTemplate.getForObject(
                "http://localhost:8080/loai-san/{id}",
                LoaiSanDTO.class,
                id
        );
        model.addAttribute("loaiSan", loaiSan);
        return "/list/quan-ly-san-bong :: modalContent";
    }

}