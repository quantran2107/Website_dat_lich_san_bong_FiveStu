package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/hoa-don/" ,produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/phan-trang")
    public ResponseEntity<Page<HoaDonDTO>> getAllHoaDon(
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc);
        Page<HoaDonDTO> hoaDonPage = hoaDonService.phanTrang(pageable);
        return ResponseEntity.ok(hoaDonPage);
    }

    @GetMapping("/search-and-filter")
    public ResponseEntity<Page<HoaDonDTO>> searchAndFilter(
            @RequestParam(required = false) Boolean loai,
            @RequestParam(required = false) String key,
            @RequestParam(required = false) Float tongTienMin,
            @RequestParam(required = false) Float tongTienMax,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc);
        Page<HoaDonDTO> hoaDonPage = hoaDonService.searchAndFilter(loai, key, tongTienMin, tongTienMax, pageable);
        return ResponseEntity.ok(hoaDonPage);
    }

}
