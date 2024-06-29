package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/" ,produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/phan-trang")
    public ResponseEntity<Page<PhieuGiamGiaDTO>> getAllPhieuGiamGia(
             @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(phieuGiamGiaService.phanTrang(pageable));
    }

}
