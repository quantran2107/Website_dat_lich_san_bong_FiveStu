package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
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

    @GetMapping("last-data")
    public ResponseEntity<?> lastData() {
        return ResponseEntity.ok(giaoCaService.lastData());
    }

    @GetMapping("ban-giao")
    public ResponseEntity<?> banGiao(HttpServletRequest request) {
        return ResponseEntity.ok(giaoCaService.banGiao(request));
    }

    @PutMapping("change-gc")
    public ResponseEntity<?> changeGc(HttpServletRequest request,@RequestBody GiaoCaFormRequest giaoCaFormRequest) {
        return giaoCaService.changeGC(request,giaoCaFormRequest);
    }

    @PostMapping("add-row")
    public ResponseEntity<?> addRow(HttpServletRequest request, @RequestBody NhanCaRequest requestBody) {
        return ResponseEntity.ok(giaoCaService.addRow(request,requestBody));
    }
}
