package com.example.DATN_WebFiveTus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhachHangNoiQuyController {

    @GetMapping("khach-hang-noi-quy")
    public String dieuHuongKhachHangNoiQuy(){
        return "client/khach-hang-noi-quy";
    }

    @GetMapping("khach-hang-noi-quy2")
    public String dieuHuongKhachHangNoiQuy2(){
        return "client/khach-hang-noi-quy2";
    }

    @GetMapping("listThamSo")
    public String dieuHuongThamSo(){
        return "list/quan-ly-tham-so";
    }
}
