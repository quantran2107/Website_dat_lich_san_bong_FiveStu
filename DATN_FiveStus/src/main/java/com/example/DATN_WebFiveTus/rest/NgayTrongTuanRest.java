package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.service.NgayTrongTuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ngay-trong-tuan")
public class NgayTrongTuanRest {

    private NgayTrongTuanService ngayTrongTuanService;

    @Autowired
    public NgayTrongTuanRest(NgayTrongTuanService ngayTrongTuanService) {
        this.ngayTrongTuanService = ngayTrongTuanService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        return ResponseEntity.ok(ngayTrongTuanService.getAll());
    }
}
