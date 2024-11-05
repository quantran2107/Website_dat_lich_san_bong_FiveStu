package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.service.LoaiSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("loai-san")
public class LoaiSanRest {

    private LoaiSanService loaiSanService;

    @Autowired
    public LoaiSanRest(LoaiSanService loaiSanService) {
        this.loaiSanService = loaiSanService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        List<LoaiSanDTO> listSanCa=loaiSanService.getAllJoinFetch();
        return ResponseEntity.ok(listSanCa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiSanDTO> getOne(@PathVariable("id") Integer id){
        LoaiSanDTO sanCaDTODetail=loaiSanService.getOne(id);
        return ResponseEntity.ok(sanCaDTODetail);
    }

    @PostMapping("")
    public ResponseEntity<LoaiSanDTO> save(@RequestBody LoaiSanDTO sanCaDTO){
        LoaiSanDTO loaiSanDTOSave=loaiSanService.save(sanCaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(loaiSanDTOSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiSanDTO> update(@PathVariable("id") Integer id ,@RequestBody LoaiSanDTO loaiSanDTO){
        LoaiSanDTO loaiSanDTOUpdate=loaiSanService.update(id,loaiSanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(loaiSanDTOUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        loaiSanService.deletedAt(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("exists")
    public ResponseEntity<Boolean> existsByTenLoaiSan(@RequestParam String tenLoaiSan) {
        Boolean exists = loaiSanService.existsByTenLoaiSan(tenLoaiSan);
        return ResponseEntity.ok(exists);
    }


}