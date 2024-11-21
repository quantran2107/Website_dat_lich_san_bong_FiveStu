package com.example.DATN_WebFiveTus.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhachHangNoiQuyController {


    @GetMapping("khach-hang-noi-quy3")
    public String dieuHuongKhachHangNoiQuy3(){
        return "client/khach-hang-noi-quy3";
    }

    @GetMapping("listThamSo")
    public String dieuHuongThamSo(){
        return "list/quan-ly-tham-so";
    }
}
