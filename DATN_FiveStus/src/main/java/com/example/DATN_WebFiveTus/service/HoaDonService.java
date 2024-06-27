package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;

import java.util.List;

public interface HoaDonService {

    List<HoaDonDTO> getAll();

    List<HoaDonDTO> getAllJoinFetch();

    HoaDonDTO getOne(Integer id);

    HoaDonDTO save(HoaDonDTO hoaDonDTO);

    HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO);

    void delete (Integer id);

    void deletedAt(Integer id);

}
