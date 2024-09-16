package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List> getAll(@PathVariable("idHoaDon") Integer idHoaDon) {
        List<HoaDonChiTietDTO> listHoaDon = hoaDonChiTietService.searchFromHoaDon(idHoaDon);
        return ResponseEntity.ok(listHoaDon);
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll2() {
        List<HoaDonChiTietDTO> listHoaDon = hoaDonChiTietService.getAllJoinFetch();
        return ResponseEntity.ok(listHoaDon);
    }

    @GetMapping("/trang-thai")
    public ResponseEntity<?> getHoaDonChiTietByTrangThai(
            @RequestParam(defaultValue = "false") String trangThai,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        System.out.println("Fetching data for page " + page + " with size " + size);
        Page<HoaDonChiTietDTO> result = hoaDonChiTietService.getHoaDonChiTietByTrangThai(trangThai, pageable);
        return ResponseEntity.ok(result);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Integer id) {
        HoaDonChiTietDTO hoaDonChiTietDTO = hoaDonChiTietService.getOneHDCT(id);
        return ResponseEntity.ok(hoaDonChiTietDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        hoaDonChiTietService.updateTrangThai(id);
       return ResponseEntity.ok().build();
    }
}
