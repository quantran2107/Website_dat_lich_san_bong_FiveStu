package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;

import java.util.List;

public interface LoaiSanService {

    List<LoaiSanDTO> getAll();

    LoaiSanDTO getOne(Integer id);

    LoaiSanDTO save(LoaiSanDTO loaiSanDTO);

    LoaiSanDTO update(Integer id, LoaiSanDTO loaiSanDTO);

    void delete (Integer id);

    List<LoaiSanDTO> getAllJoinFetch();

    void deletedAt(Integer id);

    Boolean existsByTenLoaiSan(String tenLoaiSan);
}

