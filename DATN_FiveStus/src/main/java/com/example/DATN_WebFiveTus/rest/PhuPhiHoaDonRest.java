package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhuPhiHoaDonDTO;
import com.example.DATN_WebFiveTus.entity.PhuPhiHoaDon;
import com.example.DATN_WebFiveTus.service.PhuPhiHoaDonService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/phu-phi-hoa-don")
public class PhuPhiHoaDonRest {

    @Autowired
    private PhuPhiHoaDonService phuPhiHoaDonService;

    @PostMapping("/add")
    public ResponseEntity<PhuPhiHoaDonDTO> save(@RequestBody PhuPhiHoaDonDTO phuPhiHoaDonDTO) throws MessagingException {
        PhuPhiHoaDonDTO phuPhiHoaDonDTO1 = phuPhiHoaDonService.save(phuPhiHoaDonDTO);
        return ResponseEntity.ok(phuPhiHoaDonDTO1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<PhuPhiHoaDonDTO>> getPhuPhiByHoaDonChiTietId(@PathVariable int id) {
        // Lấy danh sách dựa trên id của HoaDonChiTiet và lọc các bản ghi không bị xóa
        List<PhuPhiHoaDonDTO> phuPhiHoaDonDTOList = phuPhiHoaDonService.findByHoaDonChiTietId(id)
                .stream()
                .filter(phuPhi -> !phuPhi.isDeletedAt())  // Lọc những bản ghi có deletedAt là false
                .collect(Collectors.toList());
        return ResponseEntity.ok(phuPhiHoaDonDTOList);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<PhuPhiHoaDonDTO> updateDeletedAt(@PathVariable int id) {
        // Gọi service để cập nhật giá trị deletedAt
        PhuPhiHoaDonDTO updatedPhuPhi = phuPhiHoaDonService.updateDeletedAt(id);
        if (updatedPhuPhi != null) {
            return ResponseEntity.ok(updatedPhuPhi);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Trả về 404 nếu không tìm thấy
        }
    }

}
