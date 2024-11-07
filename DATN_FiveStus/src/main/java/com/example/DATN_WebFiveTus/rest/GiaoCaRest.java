package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.request.NhanCaRequest;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("giao-ca")
public class GiaoCaRest {

    @Autowired
    private GiaoCaService giaoCaService;

    @GetMapping("check-gc")
    public ResponseEntity<?> checkGiaoCa(HttpServletRequest request) {
        return ResponseEntity.ok(giaoCaService.checkGC(request));
    }

    @PutMapping("ban-giao")
    public ResponseEntity<?> banGiao(HttpServletRequest request) {
        return ResponseEntity.ok(giaoCaService.banGiao(request));
    }

    @PostMapping("add-row")
    public ResponseEntity<?> addRow(HttpServletRequest request, @RequestBody NhanCaRequest requestBody) {
        return ResponseEntity.ok(giaoCaService.addRow(request,requestBody));
    }
}
