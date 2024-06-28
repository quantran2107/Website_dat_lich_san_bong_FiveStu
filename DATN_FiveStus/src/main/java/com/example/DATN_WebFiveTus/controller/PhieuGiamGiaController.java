package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.rest.PhieuGiamGiaRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/web/")
public class PhieuGiamGiaController {

//    @GetMapping("/test")
//    public String hienThi() {
//        return "index";
//    }
    @Autowired
    private PhieuGiamGiaRest phieuGiamGiaRest;

    @GetMapping("/web-phieu-giam-gia")
    public String hienThi(Model model){
        model.addAttribute("listPGG",phieuGiamGiaRest.getAll().getBody());
        return "/list/quan-ly-phieu-giam-gia";
    }
}
