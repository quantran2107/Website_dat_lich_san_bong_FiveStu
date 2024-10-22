package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.config.security.CheckRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhachHangDatSanController {

    @Autowired
    private CheckRole checkRole;

    @GetMapping("/khach-hang/dat-san")
    public String hienThi(HttpServletRequest request) {
        return checkRole.checkUser(request) || checkRole.checkUser(request) || checkRole.checkUser(request) ? "/client/khach-hang-dat-san" : "redirect:/login";
    }

    @GetMapping("/khach-hang/dat-san-test")
    public String test(HttpServletRequest request) {
        return "/client/khach-hang-dat-san";
//        return checkRole.checkUser(request) || checkRole.checkUser(request) || checkRole.checkUser(request) ? "/client/khach-hang-dat-san" : "redirect:/login";
    }
}
