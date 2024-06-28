package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class PhieuGiamGiaRest {

    private final PhieuGiamGiaService phieuGiamGiaService;

    public PhieuGiamGiaRest(PhieuGiamGiaService phieuGiamGiaService) {
        this.phieuGiamGiaService = phieuGiamGiaService;
    }

    @GetMapping("/hien-thi")
    public ResponseEntity<List<PhieuGiamGiaDTO>> getAll() {
        List<PhieuGiamGiaDTO> phieuGiamGiaList = phieuGiamGiaService.getAll();
        return ResponseEntity.ok(phieuGiamGiaList);
    }
}
