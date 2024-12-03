package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//        Collections.reverse(khachHangDTOS);
        return ResponseEntity.ok(khachHangDTOS);
    }



    @GetMapping("/search")
    public ResponseEntity<Page<KhachHangDTO>> searchAndFilterKhachHang(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "all") String gender,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize) {

        // Kiểm tra và điều chỉnh page nếu nhỏ hơn 0
        if (page < 0) {
            page = 0;
        }

        // Tạo Pageable với phân trang và sắp xếp theo createdAt (giảm dần)
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));


        // Gọi service với Pageable để tìm kiếm, lọc, phân trang và sắp xếp
        Page<KhachHangDTO> result = khachHangService.searchAndFilter(query, status, gender, pageable);

        // Trả về kết quả tìm kiếm dưới dạng ResponseEntity
        return ResponseEntity.ok(result);
    }


    @GetMapping("/filter")
    public ResponseEntity<Page<KhachHangDTO>> filterKhachHang(
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "all") String gender,
            @RequestParam(defaultValue = "0") int page,  // Sử dụng page bắt đầu từ 0
            @RequestParam(defaultValue = "5") int pageSize) {  // Mặc định mỗi trang 5 mục

        // Kiểm tra và điều chỉnh page nếu nó nhỏ hơn 0
        if (page < 0) {
            page = 0;  // Đảm bảo giá trị page không nhỏ hơn 0
        }
        Page<KhachHangDTO> result = khachHangService.filter(status, gender, page, pageSize);

        return ResponseEntity.ok(result);
    }




    @GetMapping("/tim-kiem-kh")
    public KhachHangDTO findByKhachHang(@RequestParam(defaultValue = "false") String soDienThoai){
        return khachHangService.findBySoDienThoai(soDienThoai);
    }

    @GetMapping("/search-active")
    public ResponseEntity<Page<KhachHangDTO>> searchKhachHangActive(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "active") String trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<KhachHangDTO> khachHangDTOPage = khachHangService.searchActive(query, trangThai, pageable);
        return ResponseEntity.ok(khachHangDTOPage);
    }

    @PostMapping("/save2")
    public ResponseEntity<KhachHangDTO> save2(@RequestBody KhachHangDTO khachHangDTO){
        KhachHangDTO khachHangDTOSave = khachHangService.save2(khachHangDTO);
        return ResponseEntity.ok(khachHangDTOSave);
    }
    @PutMapping("/update/{email}")
    public ResponseEntity<KhachHangDTO> updateKhachHangByEmail(@PathVariable String email, @RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHangDTO updatedKhachHang = khachHangService.updateKhachHangByEmail(email, khachHangDTO);
            return new ResponseEntity<>(updatedKhachHang, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
