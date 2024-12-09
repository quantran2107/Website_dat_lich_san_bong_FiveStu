package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.ChiTietHinhThucThanhToanDTO;
import com.example.DATN_WebFiveTus.dto.HTTTDto;
import com.example.DATN_WebFiveTus.dto.PhuPhiHoaDonDTO;
import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import com.example.DATN_WebFiveTus.service.CTHTTTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("httt")
public class HinhThucThanhToanRest {

    @Autowired
    private CTHTTTService cthttService;

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getHtttForIdHD(@PathVariable int id) {
//        return ResponseEntity.ok(cthttService.getHtttById(id));
//    }
//
//    @PostMapping("add")
//    public ResponseEntity<?> addNew(@RequestBody HTTTDto htttDto){
//        return ResponseEntity.ok(cthttService.addNew(htttDto));
//    }

    @GetMapping("find-by-id-hdct/{id}")
    public ResponseEntity<?> getChiTietHTTTByHoaDonChiTietId(@PathVariable int id) {
        // Lấy danh sách dựa trên id của HoaDonChiTiet và lọc các bản ghi không bị xóa
        List<ChiTietHinhThucThanhToanDTO> chiTietHinhThucThanhToanDTOS = cthttService.findByHoaDonChiTietId(id);
        return ResponseEntity.ok(chiTietHinhThucThanhToanDTOS);
    }

}
