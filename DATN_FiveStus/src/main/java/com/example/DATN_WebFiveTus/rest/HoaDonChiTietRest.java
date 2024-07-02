package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hoa-don-chi-tiet/")
public class HoaDonChiTietRest {

    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    public HoaDonChiTietRest(HoaDonChiTietService hoaDonChiTietService) {
        this.hoaDonChiTietService = hoaDonChiTietService;
    }

    @GetMapping("hien-thi/{idHoaDon}")
    public ResponseEntity<List> getAll(@PathVariable("idHoaDon") Integer idHoaDon){
        List<HoaDonChiTietDTO> listHoaDon = hoaDonChiTietService.searchFromHoaDon(idHoaDon);
        return ResponseEntity.ok(listHoaDon);
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll2(){
        List<HoaDonChiTietDTO> listHoaDon = hoaDonChiTietService.getAllJoinFetch();
        return ResponseEntity.ok(listHoaDon);
    }

}
