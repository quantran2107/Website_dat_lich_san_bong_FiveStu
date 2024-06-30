package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("khach-hang")
public class KhachHangRest {
    private KhachHangService khachHangService;
    @Autowired

    public KhachHangRest(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }
    @GetMapping("hien-thi")
    public ResponseEntity<List>GetAll2(){
        List<KhachHangDTO>khachHangDTOS=khachHangService.getAll();
        return ResponseEntity.ok(khachHangDTOS);
    }
}
