package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
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

    @PutMapping("change-gc/{id}")
    public ResponseEntity<?> changeGC(@PathVariable("id") int id, @RequestBody GiaoCaRequest request){
        return ResponseEntity.ok(giaoCaService.changeGCN(id,request));
    }
}
