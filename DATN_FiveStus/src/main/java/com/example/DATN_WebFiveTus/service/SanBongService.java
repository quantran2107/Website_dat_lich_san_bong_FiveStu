package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.SanBongDTO;

import java.util.List;

public interface SanBongService {

    List<SanBongDTO> getAll();

    SanBongDTO getOne(Integer id);

    SanBongDTO save(SanBongDTO sanBongDTO);

    SanBongDTO update(Integer id, SanBongDTO sanBongDTO);

    void delete (Integer id);

    List<SanBongDTO> getAllJoinFetch();

    void deletedAt(Integer id);
}
