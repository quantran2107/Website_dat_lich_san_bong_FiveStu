package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("giao-ca")
public class GiaoCaRest {

    private GiaoCaService giaoCaService;

    @Autowired
    public GiaoCaRest(GiaoCaService giaoCaService) {
        this.giaoCaService = giaoCaService;
    }

    @GetMapping("for-nv/{id}")
    public ResponseEntity<?> getRowforIdNV(@PathVariable("id") int id){
        return ResponseEntity.ok(giaoCaService.getRowforId(id));
    }

    @PutMapping("change-gc")
    public ResponseEntity<?> changeGC( @RequestBody GiaoCaRequest request){
        return ResponseEntity.ok(giaoCaService.changeGCN(request));
    }


    @PostMapping("add-row")
    public ResponseEntity<?> addRow(@RequestBody GiaoCaFormRequest request){
        return ResponseEntity.ok(giaoCaService.addRow(request));
    }

    @GetMapping("nvgc")
    public ResponseEntity<?> getIDNV(HttpServletRequest request){
        return ResponseEntity.ok(giaoCaService.getIdNVG(request));
    }

    @GetMapping("check-nhan-ca")
    public ResponseEntity<?> checkNhanCa(){
        return ResponseEntity.ok(giaoCaService.checkNhanCa());
    }

    @GetMapping("getNV")
    public ResponseEntity<?> getNV(HttpServletRequest request){
        return ResponseEntity.ok(giaoCaService.getNV(request));
    }
}
