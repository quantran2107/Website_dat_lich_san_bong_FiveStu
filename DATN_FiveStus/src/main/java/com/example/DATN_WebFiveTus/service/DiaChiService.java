package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DiaChiDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.entity.DiaChi;

import java.util.List;

public interface DiaChiService {
    List<DiaChiDTO> getAll();

    DiaChiDTO getOne(Integer id);

    DiaChiDTO save(DiaChiDTO diaChiDTO);

    DiaChiDTO update(Integer id,DiaChiDTO diaChiDTO);

    void delete (Integer id);

    List<DiaChiDTO> getAllJoinFetch();
}
