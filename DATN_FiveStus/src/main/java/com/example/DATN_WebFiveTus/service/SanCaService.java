package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;

import java.util.List;

public interface SanCaService {

    List<SanCaDTO> getAll();

    List<SanCaDTO> getAllJoinFetch();

    SanCaDTO getOne(Integer id);

    SanCaDTO save(SanCaDTO sanCaDTO);

    SanCaDTO update(Integer id, SanCaDTO sanCaDTO);

    void delete (Integer id);

    void deletedAt(Integer id);
}
