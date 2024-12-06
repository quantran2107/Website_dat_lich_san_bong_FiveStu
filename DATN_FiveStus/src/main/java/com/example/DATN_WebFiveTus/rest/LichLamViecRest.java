package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.LichLamViecDTO;
import com.example.DATN_WebFiveTus.dto.request.LichLamViecSearchRequest;
import com.example.DATN_WebFiveTus.entity.LichLamViec;
import com.example.DATN_WebFiveTus.service.LichLamViecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("lich-lam-viec")
public class LichLamViecRest {

    @Autowired
    private LichLamViecService lichLamViecService;

    @GetMapping("hien-thi")
    public ResponseEntity<?> hienThi(@RequestParam(required = false) String date,
                                     @RequestParam(required = false,defaultValue = "Chọn ca") String status){
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString(); // Đổi sang kiểu String của ngày hôm nay
        }
        return ResponseEntity.ok(lichLamViecService.getAll(date,status));
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody LichLamViecDTO lichLamViecDto){
        return ResponseEntity.ok(lichLamViecService.add(lichLamViecDto));
    }
    @PostMapping("upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file){

        if(file.isEmpty()){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(lichLamViecService.addMore(file));
    }
}
