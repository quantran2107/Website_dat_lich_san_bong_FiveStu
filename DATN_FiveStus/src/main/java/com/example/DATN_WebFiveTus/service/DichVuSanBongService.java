package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DichVuSanBongDTO;

import java.util.List;

public interface DichVuSanBongService {

    List<DichVuSanBongDTO> getAll();

    DichVuSanBongDTO getOne(Integer id);

    DichVuSanBongDTO save(DichVuSanBongDTO dichVuSanBongDTO);

    DichVuSanBongDTO update(Integer id, DichVuSanBongDTO dichVuSanBongDTO);

    List<DichVuSanBongDTO> findByIdHDCT(Integer idHDCT);

    void delete(Integer id);
}
