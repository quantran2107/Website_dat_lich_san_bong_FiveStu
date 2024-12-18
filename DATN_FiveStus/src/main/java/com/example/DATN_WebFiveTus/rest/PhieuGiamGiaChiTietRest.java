package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaChiTietDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGiaChiTiet;
import com.example.DATN_WebFiveTus.service.Imp.PhieuGiamGiaChiTietServiceImp;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaChiTietService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-phieu-giam-gia-chi-tiet/")
@CrossOrigin("*") // Adjust as per your CORS policy
public class PhieuGiamGiaChiTietRest {

    @Autowired
    private PhieuGiamGiaChiTietServiceImp serviceImp;

    @Autowired
    private PhieuGiamGiaChiTietService phieuGiamGiaChiTietService;

    @GetMapping("hien-thi")
    public ResponseEntity<List<PhieuGiamGiaChiTietDTO>> getAll() {
        List<PhieuGiamGiaChiTietDTO> phieuGiamGiaCTList = serviceImp.getAll();
        return ResponseEntity.ok(phieuGiamGiaCTList);
    }

    @PostMapping("save")
    public ResponseEntity<PhieuGiamGiaChiTietDTO> save(@RequestBody PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) throws MessagingException {
        PhieuGiamGiaChiTietDTO savedPhieuGiamGiaCT = serviceImp.save(phieuGiamGiaChiTietDTO);

        return ResponseEntity.ok(savedPhieuGiamGiaCT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaChiTietDTO> update(@PathVariable("id") Integer id, @RequestBody PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        PhieuGiamGiaChiTietDTO savedPhieuGiamGiaCT = serviceImp.update(id, phieuGiamGiaChiTietDTO);
        return ResponseEntity.status(HttpStatus.OK).body(savedPhieuGiamGiaCT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaChiTietDTO> getOne(@PathVariable("id") Integer id) {
        PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO = serviceImp.getOne(id);
        return ResponseEntity.ok(phieuGiamGiaChiTietDTO);
    }

    @PutMapping("/{id}/chuyen-sang-ca-nhan")
    public ResponseEntity<?> updateToCaNhan(@PathVariable("id") Integer idPhieuGiamGia,
                                            @RequestBody List<Integer> khachHangIds) {
        serviceImp.updatePhieuGiamGiaToCaNhan(idPhieuGiamGia, khachHangIds);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @GetMapping("/pggct/{idPhieuGiamGia}")
    public ResponseEntity<List<PhieuGiamGiaChiTietDTO>> findAllPGGCT(@PathVariable("idPhieuGiamGia") Integer idPhieuGiamGia) {
        List<PhieuGiamGiaChiTietDTO> phieuGiamGiaChiTietList = serviceImp.findByIdPGG(idPhieuGiamGia);
        return ResponseEntity.ok(phieuGiamGiaChiTietList);
    }

    @GetMapping("/{id}/khach-hang/{idKhachHang}")
    public ResponseEntity<?> findByIdvaIdKhachHang(
            @PathVariable("id") Integer id,
            @PathVariable("idKhachHang") Integer idKhachHang) {
        PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO = serviceImp.findByIdPGGAndKhachHang(id, idKhachHang);
        return ResponseEntity.ok(phieuGiamGiaChiTietDTO);
    }

    @PutMapping("/{id}/khach-hang/{idKhachHang}")
    public ResponseEntity<Void> updateDeletedAt(
            @PathVariable("id") Integer id,
            @PathVariable("idKhachHang") Integer idKhachHang,
            @RequestBody Boolean deletedAt) {
        serviceImp.updateDeletedAt(id, idKhachHang, deletedAt);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/pgg-cong-khai")
    public ResponseEntity<?> getAllPGGCongKhai(@RequestParam("tongTien") Double tongTien,@RequestParam(required = false) String keyword) {
        List<PhieuGiamGiaChiTietDTO> phieuGiamGiaList = phieuGiamGiaChiTietService.getAllPGGCTCongKhai(tongTien,keyword);
        return ResponseEntity.ok(phieuGiamGiaList);
    }

    @GetMapping("/pgg-ca-nhan/{idKhachHang}")
    public ResponseEntity<?> getAllPGGCaNhan(@PathVariable("idKhachHang") Integer idKhachHang,@RequestParam("tongTien") Double tongTien,@RequestParam(required = false) String keyword) {
        List<PhieuGiamGiaChiTietDTO> phieuGiamGiaList = phieuGiamGiaChiTietService.getAllPGGCTCaNhan(idKhachHang,tongTien,keyword);
        return ResponseEntity.ok(phieuGiamGiaList);
    }

    @PutMapping("/{id}/ket-thuc")
    public ResponseEntity<Void> endPhieuGiamGia(@PathVariable("id") Integer idPhieuGiamGia) {
        serviceImp.updatePhieuGiamGiaStatusToEnded(idPhieuGiamGia);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
