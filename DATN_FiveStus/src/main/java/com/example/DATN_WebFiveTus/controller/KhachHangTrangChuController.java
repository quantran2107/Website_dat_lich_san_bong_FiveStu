package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KhachHangTrangChuController {
    @GetMapping("/khach-hang/trang-chu")
    public String hienThi(){
        return "client/khach-hang-index";
    }
}
