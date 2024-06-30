package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.service.SanBongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class SanBongController {

    private SanBongService sanBongService;

    private RestTemplate restTemplate;

    @Autowired
    public SanBongController(SanBongService sanBongService, RestTemplate restTemplate) {
        this.sanBongService = sanBongService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listSanBong")
    public String HienThi(Model model) {
        model.addAttribute("listSB", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/san-bong/hien-thi",
                SanBongDTO[].class
        )));

        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/loai-san/hien-thi",
                LoaiSanDTO[].class
        )));


        model.addAttribute("sanBong",new SanBongDTO());
        model.addAttribute("loaiSan",new LoaiSanDTO());
        return "/list/quan-ly-san-bong";
    }

    @PostMapping("/sanBong/add")
    public String add(@ModelAttribute("sanBong") SanBongDTO sanBongDTO){
        sanBongService.save(sanBongDTO);
        return "redirect:/listSanBong";
    }
}
