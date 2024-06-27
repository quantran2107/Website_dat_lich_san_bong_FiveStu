package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("hoa-don")
public class HoaDonRest {

    private HoaDonService hoaDonService;

    @Autowired
    public HoaDonRest(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        List<HoaDonDTO> listHoaDon = hoaDonService.getAllJoinFetch();
        return ResponseEntity.ok(listHoaDon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDonDTO> getOne(@PathVariable("id") Integer id){
        HoaDonDTO hoaDonDTODetail = hoaDonService.getOne(id);
        return ResponseEntity.ok(hoaDonDTODetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        hoaDonService.deletedAt(id);
        return ResponseEntity.ok().build();
    }
}
