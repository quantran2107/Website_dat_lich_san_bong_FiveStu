package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaChiTietDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.service.Imp.PhieuGiamGiaChiTietServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("hien-thi")
    public ResponseEntity<List<PhieuGiamGiaChiTietDTO>> getAll() {
        List<PhieuGiamGiaChiTietDTO> phieuGiamGiaCTList = serviceImp.getAll();
        return ResponseEntity.ok(phieuGiamGiaCTList);
    }

    @PostMapping("save")
    public ResponseEntity<PhieuGiamGiaChiTietDTO> save(@RequestBody PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        PhieuGiamGiaChiTietDTO savedPhieuGiamGiaCT = serviceImp.save(phieuGiamGiaChiTietDTO);
        return ResponseEntity.ok(savedPhieuGiamGiaCT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaChiTietDTO> update(@PathVariable("id") Integer id, @RequestBody PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        PhieuGiamGiaChiTietDTO savedPhieuGiamGiaCT = serviceImp.save(phieuGiamGiaChiTietDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPhieuGiamGiaCT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaChiTietDTO> getOne(@PathVariable("id") Integer id) {
        PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO = serviceImp.getOne(id);
        return ResponseEntity.ok(phieuGiamGiaChiTietDTO);
    }

    @GetMapping("/pggct/{idPhieuGiamGia}")
    public ResponseEntity<List<PhieuGiamGiaChiTietDTO>> findAllPGGCT(@PathVariable("idPhieuGiamGia") Integer idPhieuGiamGia) {
        List<PhieuGiamGiaChiTietDTO> phieuGiamGiaChiTietList = serviceImp.findByIdPGG(idPhieuGiamGia);
        return ResponseEntity.ok(phieuGiamGiaChiTietList);
    }

}
