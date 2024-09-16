package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("khach-hang")
public class KhachHangRest {

    private final KhachHangService khachHangService;

    @Autowired
    public KhachHangRest(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List<KhachHangDTO>> getAll() {
        List<KhachHangDTO> khachHangDTOS = khachHangService.getAll();
        return ResponseEntity.ok(khachHangDTOS);
    }

    @GetMapping("hien-thi-phan-trang")
    public ResponseEntity<Page<KhachHangDTO>> getAllPaginated(@PageableDefault(size = 5) Pageable pageable) {
        Page<KhachHangDTO> khachHangDTOS = khachHangService.getAll(pageable);
        return ResponseEntity.ok(khachHangDTOS);
    }

    @GetMapping("/search")
    public List<KhachHangDTO> searchKhachHang(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        return khachHangService.search(query, page, pageSize);
    }

    @GetMapping("/filter")
    public List<KhachHangDTO> filterKhachHang(
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "all") String gender,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        return khachHangService.filter(status, gender, page, pageSize);
    }

    @GetMapping("/tim-kiem-kh")
    public KhachHangDTO findByKhachHang(@RequestParam(defaultValue = "false") String soDienThoai){
        return khachHangService.findBySoDienThoai(soDienThoai);
    }

}
