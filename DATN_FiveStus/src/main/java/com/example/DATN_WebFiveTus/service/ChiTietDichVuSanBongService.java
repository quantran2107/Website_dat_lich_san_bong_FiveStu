package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ChiTietDichVuSanBongDTO;

import java.util.List;

public interface ChiTietDichVuSanBongService {

    List<ChiTietDichVuSanBongDTO> getAll();

    ChiTietDichVuSanBongDTO getOne(Integer id);

    ChiTietDichVuSanBongDTO save(ChiTietDichVuSanBongDTO chiTietDichVuSanBongDTO);

    ChiTietDichVuSanBongDTO update(Integer id, ChiTietDichVuSanBongDTO chiTietDichVuSanBongDTO);

    void delete(Integer id);
}
