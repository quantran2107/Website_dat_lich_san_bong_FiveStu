package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.config.security.CheckRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhanVienController {

    @Autowired
    private CheckRole checkRole;

    @GetMapping("/quan-ly-nhan-vien")
    public String hienThi(HttpServletRequest request) {
        return checkRole.checkAdmin(request) || checkRole.checkEmployee(request) ||checkRole.checkManager(request) ? "/list/nhan-vien/quan-ly-nhan-vien":"redirect:/login";
    }

}



