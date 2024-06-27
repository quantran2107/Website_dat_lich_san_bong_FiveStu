package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("phieu_giam_gia")
public class PhieuGiamGiaRest {
    private PhieuGiamGiaService phieuGiamGiaService;

    public PhieuGiamGiaRest(PhieuGiamGiaService phieuGiamGiaService) {
        this.phieuGiamGiaService = phieuGiamGiaService;
    }
    @GetMapping("hien-thi")
    public ResponseEntity<List>getAll(){
        List<PhieuGiamGiaDTO>phieuGiamGiaList=phieuGiamGiaService.getAll();
        return ResponseEntity.ok(phieuGiamGiaList);

    }
}
