package com.example.DATN_WebFiveTus.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhachHangTrangChuController {
    @GetMapping("/khach-hang/trang-chu")
    public String hienThi(){
        return "client/khach-hang-index";
    }
}
