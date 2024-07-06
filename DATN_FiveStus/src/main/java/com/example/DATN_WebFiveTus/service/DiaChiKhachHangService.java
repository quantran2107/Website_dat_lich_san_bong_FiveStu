package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;

import java.util.List;

public interface DiaChiKhachHangService {
    List<DiaChiKhachHangDTO> getAll();

    DiaChiKhachHangDTO getOne(Integer id);

    DiaChiKhachHangDTO save(DiaChiKhachHangDTO diaChiKhachHangDTO);

    DiaChiKhachHangDTO update(Integer id,DiaChiKhachHangDTO diaChiKhachHangDTO);

    void delete (Integer id);

    List<DiaChiKhachHangDTO> getAllJoinFetch();
}
