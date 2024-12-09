package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.PhuPhiHoaDonDTO;
import com.example.DATN_WebFiveTus.entity.PhuPhiHoaDon;

import java.util.List;

public interface PhuPhiHoaDonService {
    PhuPhiHoaDonDTO save(PhuPhiHoaDonDTO phuPhiHoaDonDTO);
    String generateMa();

    List<PhuPhiHoaDonDTO> findByHoaDonChiTietId(int hoaDonChiTietId);

    PhuPhiHoaDonDTO updateDeletedAt(int id);
}
