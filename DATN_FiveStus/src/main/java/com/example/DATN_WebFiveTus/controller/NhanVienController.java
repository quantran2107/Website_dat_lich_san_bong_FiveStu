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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/quan-ly-nhan-vien")
    public String hienThi(Model model) {
        model.addAttribute("listNV", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/nhan-vien/hien-thi",
                NhanVienDTO[].class
        )));
        return "/list/nhan-vien/quan-ly-nhan-vien";
    }

    @PostMapping("/them-nhan-vien")
    public String addNhanVien(@ModelAttribute NhanVienDTO nhanVienDTO) {
        nhanVienService.save(nhanVienDTO);
        return "redirect:/quan-ly-nhan-vien";
    }

    @PostMapping("/update-nhan-vien")
    public String updateNhanVien(@ModelAttribute NhanVienDTO nhanVienDTO) {

        nhanVienService.updateNV(nhanVienDTO.getId(),nhanVienDTO);
        return "redirect:/quan-ly-nhan-vien";
    }
}


