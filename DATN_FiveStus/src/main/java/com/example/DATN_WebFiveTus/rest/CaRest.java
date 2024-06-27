package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.service.CaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ca")
public class CaRest {

    private CaService caService;

    public CaRest(CaService caService) {
        this.caService = caService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        System.out.println(caService.getAll().toString());
        return ResponseEntity.ok(caService.getAll());
    }
}
