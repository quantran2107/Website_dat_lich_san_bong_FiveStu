package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<HoaDonChiTietDTO> getHoaDonChiTietByTrangThai(String trangThai, Pageable pageable);

    HoaDonChiTietDTO getOneHDCT(Integer id);

}
