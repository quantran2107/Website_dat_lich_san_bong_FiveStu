package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.service.DiaChiKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/quan-ly-khach-hang")
public class DiaChiKhachHangController {

    @Autowired
    private DiaChiKhachHangService diaChiKhachHangService;

    @PostMapping("/them-dia-chi")
    public ResponseEntity<String> addAddress(@RequestParam("idKhachHang") Integer idKhachHang,
                                             @ModelAttribute DiaChiKhachHangDTO diaChiKhachHangDTO) {
        try {
            diaChiKhachHangDTO.setIdKhachHang(idKhachHang);
            diaChiKhachHangDTO.setDeletedAt(false);
            diaChiKhachHangService.save(diaChiKhachHangDTO);
            return ResponseEntity.ok("Thêm địa chỉ thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi thêm địa chỉ");
        }
    }
    @PostMapping("/cap-nhat-dia-chi")
    public ResponseEntity<String> updateAddress(@RequestParam("id") Integer id, @ModelAttribute DiaChiKhachHangDTO diaChiKhachHangDTO) {
        try {
            // Lấy địa chỉ từ id
            DiaChiKhachHangDTO diaChi = diaChiKhachHangService.getOne(id);

            // Lấy idKhachHang từ địa chỉ
            Integer idKhachHang = diaChi.getIdKhachHang();

            // Cập nhật địa chỉ
            diaChiKhachHangService.update(id, diaChiKhachHangDTO);

            // Trả về thông báo thành công
            return ResponseEntity.ok("Cập nhật địa chỉ thành công!");
        } catch (Exception e) {
            // Trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi cập nhật địa chỉ");
        }
    }


    @GetMapping("/dia-chi/{id}")
    @ResponseBody
    public DiaChiKhachHangDTO getAddress(@PathVariable Integer id) {
        return diaChiKhachHangService.getOne(id);
    }
}
