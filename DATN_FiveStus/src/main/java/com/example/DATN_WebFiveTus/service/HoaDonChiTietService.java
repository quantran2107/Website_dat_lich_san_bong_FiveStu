package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;

import java.util.List;

public interface HoaDonChiTietService {

    List<HoaDonChiTietDTO> getAll();

    List<HoaDonChiTietDTO> getAllJoinFetch();

    HoaDonChiTietDTO getOne(Integer id);

    HoaDonChiTietDTO save(HoaDonChiTietDTO hoaDonChiTietDTO);

    HoaDonChiTietDTO update(Integer id, HoaDonChiTietDTO hoaDonChiTietDTO);

    void delete (Integer id);

    void deletedAt(Integer id);

    List<HoaDonChiTietDTO> searchFromHoaDon(Integer idHoaDon);

}
