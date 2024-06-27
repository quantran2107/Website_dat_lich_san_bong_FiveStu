package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.service.ChiTietDichVuSanBongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chi-tiet-dich-vu-san-bong")
public class ChiTietDichVuSanBongRest {

    private ChiTietDichVuSanBongService chiTietDichVuSanBongService;

    @Autowired
    public ChiTietDichVuSanBongRest(ChiTietDichVuSanBongService chiTietDichVuSanBongService) {
        this.chiTietDichVuSanBongService = chiTietDichVuSanBongService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        return ResponseEntity.ok(chiTietDichVuSanBongService.getAll());
    }
}
