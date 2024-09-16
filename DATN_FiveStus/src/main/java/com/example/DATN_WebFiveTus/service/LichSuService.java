package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.LichSuDTO;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;

import java.util.List;

public interface LichSuService {

    List<LichSuDTO> getAll();

    LichSuDTO save(LichSuDTO lichSuDTO);

    LichSuDTO update(Integer id, LichSuDTO lichSuDTO);

    void checkin(HoaDonChiTietDTO hoaDonChiTietDTO);
}
