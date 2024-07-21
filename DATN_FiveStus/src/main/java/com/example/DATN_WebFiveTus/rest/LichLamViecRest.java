package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.LichLamViecDTO;
import com.example.DATN_WebFiveTus.entity.LichLamViec;
import com.example.DATN_WebFiveTus.service.LichLamViecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("lich-lam-viec")
public class LichLamViecRest {

    @Autowired
    private LichLamViecService lichLamViecService;

    @GetMapping("hien-thi")
    public ResponseEntity<?> hienThi(){
        return ResponseEntity.ok(lichLamViecService.getAll());
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
