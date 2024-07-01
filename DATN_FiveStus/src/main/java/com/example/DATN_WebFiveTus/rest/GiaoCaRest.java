package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("giao-ca")
public class GiaoCaRest {

    private GiaoCaService giaoCaService;

    @Autowired
    public GiaoCaRest(GiaoCaService giaoCaService) {
        this.giaoCaService = giaoCaService;
    }

    @GetMapping("hien-thi")
    public List<GiaoCaDTO> getAll(){
        return giaoCaService.getAll();
    }
}
