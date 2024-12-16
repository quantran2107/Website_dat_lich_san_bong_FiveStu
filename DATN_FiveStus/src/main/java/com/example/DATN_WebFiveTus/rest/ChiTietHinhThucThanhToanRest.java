package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.ChiTietHinhThucThanhToanDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.service.CTHTTTService;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chi-tiet-hinh-thuc-thanh-toan/")
public class ChiTietHinhThucThanhToanRest {

    private CTHTTTService chiCthtttService;

    @Autowired
    public ChiTietHinhThucThanhToanRest(CTHTTTService chiCthtttService) {
        this.chiCthtttService = chiCthtttService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save2(@RequestBody ChiTietHinhThucThanhToanDTO chiTietHinhThucThanhToanDTO, HttpServletRequest request) {
        ChiTietHinhThucThanhToanDTO chiTHTTTSave = chiCthtttService.save(chiTietHinhThucThanhToanDTO,request);
        return ResponseEntity.ok(chiTHTTTSave);
    }

    @GetMapping("find-by-id-hdct/{id}")
    public ResponseEntity<?> getChiTietHTTTByHoaDonChiTietId(@PathVariable int id) {
        // Lấy danh sách dựa trên id của HoaDonChiTiet và lọc các bản ghi không bị xóa
        List<ChiTietHinhThucThanhToanDTO> chiTietHinhThucThanhToanDTOS = chiCthtttService.findByHoaDonChiTietId(id);
        return ResponseEntity.ok(chiTietHinhThucThanhToanDTOS);
    }

    @PutMapping("xoa/{id}")
    public ResponseEntity<?> deletedSoft(@PathVariable Integer id){
        chiCthtttService.deletedSoft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
