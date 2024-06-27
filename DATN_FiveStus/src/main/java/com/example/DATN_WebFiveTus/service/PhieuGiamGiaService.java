package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;

import java.util.List;

public interface PhieuGiamGiaService {
    List<PhieuGiamGiaDTO> getAll();

    PhieuGiamGiaDTO getOne(Integer id);

    PhieuGiamGiaDTO save(PhieuGiamGiaDTO phieuGiamGiaDTO);

    PhieuGiamGiaDTO update(Integer id,PhieuGiamGiaDTO phieuGiamGiaDTO);

    void delete (Integer id);
}
