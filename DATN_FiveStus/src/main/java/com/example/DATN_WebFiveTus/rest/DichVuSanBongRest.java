package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.DichVuSanBongDTO;
import com.example.DATN_WebFiveTus.service.DichVuSanBongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/{id}")
    public ResponseEntity<DichVuSanBongDTO> getOne(@PathVariable("id") Integer id){
        return ResponseEntity.ok(dichVuSanBongService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<DichVuSanBongDTO> save(@RequestBody DichVuSanBongDTO dichVuSanBongDTO){
        return ResponseEntity.ok(dichVuSanBongService.save(dichVuSanBongDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DichVuSanBongDTO> update(@PathVariable("id") Integer id , @RequestBody DichVuSanBongDTO dichVuSanBongDTO){
        return ResponseEntity.ok(dichVuSanBongService.update(id,dichVuSanBongDTO));
    }
}
