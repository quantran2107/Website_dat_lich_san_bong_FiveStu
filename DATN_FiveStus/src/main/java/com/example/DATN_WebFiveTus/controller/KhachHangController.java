package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@RequestMapping("/quan-ly-khach-hang")
public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String hienThi(Model model) {
        model.addAttribute("listKH", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/khach-hang/hien-thi",
                KhachHangDTO[].class
        )));
        model.addAttribute("khachHangDTO", new KhachHangDTO());
        return "/list/quan-ly-khach-hang";
    }

    @PostMapping("/them")
    public String addKhachHang(@ModelAttribute KhachHangDTO khachHangDTO) {
        khachHangService.save(khachHangDTO);
        return "redirect:/quan-ly-khach-hang";
    }
    @GetMapping("/detail")
    public String detailKH(@RequestParam("id") Integer id, Model model){
        KhachHangDTO khachHangDTO = khachHangService.findById(id);
        if (khachHangDTO != null) {
            model.addAttribute("khachHang", khachHangDTO);
            return "/detail/khach-hang-detail";
        } else {
            return "redirect:/quan-ly-khach-hang";
        }
    }
}


