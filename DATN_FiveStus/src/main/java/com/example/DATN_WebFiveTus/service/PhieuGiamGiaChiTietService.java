package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaChiTietDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGiaChiTiet;

import java.util.List;

public interface PhieuGiamGiaChiTietService {
    List<PhieuGiamGiaChiTietDTO> getAll();
    PhieuGiamGiaChiTietDTO save(PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO);
    PhieuGiamGiaChiTietDTO update(Integer id, PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO);
    PhieuGiamGiaChiTietDTO getOne(Integer id);
    List<PhieuGiamGiaChiTietDTO> findByIdPGG(Integer ids);
    List<PhieuGiamGiaChiTietDTO> findByIdKhachHang(Integer idKhachHang);
    List<PhieuGiamGiaChiTietDTO> getAllPGGCTCongKhai(Double tongTien);
    List<PhieuGiamGiaChiTietDTO> getAllPGGCTCaNhan(Integer idKhachHang,Double tongTien);
    void updateDeletedAt(Integer id, Integer idKhachHang, Boolean deletedAt);
}
