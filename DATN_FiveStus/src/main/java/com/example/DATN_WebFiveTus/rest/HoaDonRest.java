package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/hoa-don/" ,produces = MediaType.APPLICATION_JSON_VALUE)
public class HoaDonRest {
    @Autowired
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
    @GetMapping("search-for-nv/{id}")
    public ResponseEntity<?> getHoaDonForIdNV(@PathVariable("id") int id){
        return ResponseEntity.ok(hoaDonService.getHDforNV(id));
    }

    @GetMapping("/search-and-filter")
    public ResponseEntity<Page<HoaDonDTO>> searchAndFilter(
            @RequestParam(required = false) Boolean loai,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String key,
            @RequestParam(required = false) Float tongTienMin,
            @RequestParam(required = false) Float tongTienMax,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime ngayTaoMin,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime ngayTaoMax,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc);
        Page<HoaDonDTO> hoaDonPage = hoaDonService.searchAndFilter(loai, trangThai, key, tongTienMin, tongTienMax, ngayTaoMin, ngayTaoMax, pageable);
        return ResponseEntity.ok(hoaDonPage);
    }

    @PostMapping("/save")
    public ResponseEntity<HoaDonDTO> save(@RequestBody HoaDonDTO hoaDonDTO){
        HoaDonDTO hoaDonDTOSave = hoaDonService.save(hoaDonDTO);
        return ResponseEntity.ok(hoaDonDTOSave);
    }

    // API để cập nhật thanh toán dựa vào idHoaDonChiTiet
    @PutMapping("/update-thanh-toan/{idHoaDonChiTiet}")
    public ResponseEntity<String> updateThanhToan(@PathVariable Integer idHoaDonChiTiet) {
        try {
            // Gọi service để cập nhật thanh toán
            hoaDonService.updateThanhToan(idHoaDonChiTiet);
            return ResponseEntity.ok("Cập nhật thanh toán thành công cho hóa đơn chi tiết với id: " + idHoaDonChiTiet);
        } catch (RuntimeException e) {
            // Nếu có lỗi xảy ra, trả về thông báo lỗi và mã HTTP 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/huy-lich-dat/{id}")
    public ResponseEntity<HoaDonDTO> huyLichDat(@PathVariable Integer id){
        HoaDonDTO hoaDonDTO = hoaDonService.huyLichDat(id);
        return ResponseEntity.ok(hoaDonDTO);
    }

    @GetMapping("/get-nhan-vien-trong-ca")
    public ResponseEntity<NhanVienDTO> getNhanVienTrongCa(HttpServletRequest request){
        return ResponseEntity.ok(hoaDonService.getNhanVienTrongCa(request));
    }

    @PutMapping("/update-trang-thai/{idHoaDon}")
    public ResponseEntity<Void> updateTrangThaiHoaDon(@PathVariable Integer idHoaDon, @RequestParam String trangThai) {
        hoaDonService.updateTrangThaiHoaDon(idHoaDon, trangThai);
        return ResponseEntity.ok().build();
    }

}
