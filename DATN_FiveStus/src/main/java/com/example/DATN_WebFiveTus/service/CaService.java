package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;

import java.util.List;

public interface CaService {

    List<CaDTO> getAll();

    CaDTO getOne(Integer id);

    CaDTO save(CaDTO caDTO);

    CaDTO update(Integer id, CaDTO caDTO);

    void delete (Integer id);
}
