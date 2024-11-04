package com.example.DATN_WebFiveTus.controller.client;

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
//        return checkRole.checkUser(request) || checkRole.checkUser(request) || checkRole.checkUser(request) ? "/client/khach-hang-dat-san" : "redirect:/login";
        return "/client/khach-hang-dat-lich";
    }

}
