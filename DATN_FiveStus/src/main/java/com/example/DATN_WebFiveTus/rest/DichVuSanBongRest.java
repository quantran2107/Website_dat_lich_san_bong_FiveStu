package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.service.DichVuSanBongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dich-vu-san-bong")
public class DichVuSanBongRest {

    private DichVuSanBongService dichVuSanBongService;

    @Autowired
    public DichVuSanBongRest(DichVuSanBongService dichVuSanBongService) {
        this.dichVuSanBongService = dichVuSanBongService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        return ResponseEntity.ok(dichVuSanBongService.getAll());
    }
}
