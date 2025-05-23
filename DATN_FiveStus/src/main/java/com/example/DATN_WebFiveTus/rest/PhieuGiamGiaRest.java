package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api-phieu-giam-gia/")
@CrossOrigin("*") // Adjust as per your CORS policy
public class PhieuGiamGiaRest {

    private final PhieuGiamGiaService phieuGiamGiaService;
    private final PagedResourcesAssembler<PhieuGiamGiaDTO> pagedResourcesAssembler;

    @Autowired
    public PhieuGiamGiaRest(PhieuGiamGiaService phieuGiamGiaService,
                            PagedResourcesAssembler<PhieuGiamGiaDTO> pagedResourcesAssembler) {
        this.phieuGiamGiaService = phieuGiamGiaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/phan-trang")
    public ResponseEntity<Page<PhieuGiamGiaDTO>> layPhanTrang(
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc);
        Page<PhieuGiamGiaDTO> trangPhieuGiamGia = phieuGiamGiaService.phanTrang(pageable);
        return ResponseEntity.ok(trangPhieuGiamGia);
    }


    @GetMapping("/hien-thi")
    public ResponseEntity<List<PhieuGiamGiaDTO>> getAll() {
        List<PhieuGiamGiaDTO> phieuGiamGiaList = phieuGiamGiaService.getAll();
        return ResponseEntity.ok(phieuGiamGiaList);
    }

    @GetMapping("/fill-PGG")
    public ResponseEntity<List<PhieuGiamGiaDTO>> fillPGG(@RequestParam("tongTien") Double tongTien) {
        List<PhieuGiamGiaDTO> phieuGiamGiaList = phieuGiamGiaService.fillPGG(tongTien);
        return ResponseEntity.ok(phieuGiamGiaList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getOne(@PathVariable("id") Integer id) {
        PhieuGiamGiaDTO phieuGiamGiaDTO = phieuGiamGiaService.getOne(id);
        return ResponseEntity.ok(phieuGiamGiaDTO);
    }

    @PostMapping("save")
    public ResponseEntity<PhieuGiamGiaDTO> save(@RequestBody PhieuGiamGiaDTO phieuGiamGiaDTO) throws MessagingException {
        PhieuGiamGiaDTO savedPhieuGiamGia = phieuGiamGiaService.save(phieuGiamGiaDTO);
        return ResponseEntity.ok(savedPhieuGiamGia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> update(@PathVariable("id") Integer id, @RequestBody PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGiaDTO phieuGiamGiaDTODetail = phieuGiamGiaService.update(id,phieuGiamGiaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuGiamGiaDTODetail);
    }

    @PutMapping("update2/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> update2(@PathVariable("id") Integer id, @RequestBody PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGiaDTO phieuGiamGiaDTODetail = phieuGiamGiaService.update2(id,phieuGiamGiaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuGiamGiaDTODetail);
    }

    @PutMapping("/trang-thai/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer id, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("trangThai");
        if (newStatus == null) {
            return ResponseEntity.badRequest().body("Thiếu trường 'trangThai' trong yêu cầu");
        }
        try {
            phieuGiamGiaService.updateStatus(id, newStatus);
            return ResponseEntity.ok().body("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cập nhật trạng thái thất bại");
        }
    }

    @GetMapping("/search-and-filter")
    public ResponseEntity<Page<PhieuGiamGiaDTO>> searchAndFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean doiTuongApDung,
            @RequestParam(required = false) Boolean hinhThucGiamGia,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));

        // Chuyển đổi java.util.Date sang java.sql.Date
        java.sql.Date sqlNgayBatDau = ngayBatDau != null ? new java.sql.Date(ngayBatDau.getTime()) : null;
        java.sql.Date sqlNgayKetThuc = ngayKetThuc != null ? new java.sql.Date(ngayKetThuc.getTime()) : null;

        Page<PhieuGiamGiaDTO> phieuGiamGiaPage = phieuGiamGiaService.searchPhieuGiamGia(
                keyword, doiTuongApDung, hinhThucGiamGia, trangThai, sqlNgayBatDau, sqlNgayKetThuc, sortedPageable);

        return ResponseEntity.ok(phieuGiamGiaPage);
    }

    @GetMapping("/check-duplicate/{maPhieuGiamGia}")
    public ResponseEntity<Boolean> checkDuplicate(@PathVariable String maPhieuGiamGia) {
        boolean isDuplicate = phieuGiamGiaService.isDuplicateMaPhieuGiamGia(maPhieuGiamGia);
        return ResponseEntity.ok(isDuplicate);
    }

}
