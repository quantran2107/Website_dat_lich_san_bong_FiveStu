package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.LichSuDTO;
import com.example.DATN_WebFiveTus.service.LichSuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("lich-su")
public class LichSuRest {

    private LichSuService lichSuService;

    @Autowired
    public LichSuRest(LichSuService lichSuService) {
        this.lichSuService = lichSuService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(lichSuService.getAll());
    }

    @PostMapping
    public ResponseEntity<LichSuDTO> save(@RequestBody LichSuDTO lichSuDTO) {
        return ResponseEntity.ok(lichSuService.save(lichSuDTO));
    }

    @PostMapping("/checkinLichSu")
    public ResponseEntity<Void> checkIn(@RequestBody HoaDonChiTietDTO hoaDonChiTietDTO) {
        lichSuService.checkin(hoaDonChiTietDTO);
        return ResponseEntity.ok().build();
    }
}
