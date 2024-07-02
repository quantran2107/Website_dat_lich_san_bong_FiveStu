package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("nhan-vien")
public class NhanVienRest {

    private NhanVienService nhanVienService;
    @Autowired
    public NhanVienRest(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        return ResponseEntity.ok(nhanVienService.getAll());
    }

}