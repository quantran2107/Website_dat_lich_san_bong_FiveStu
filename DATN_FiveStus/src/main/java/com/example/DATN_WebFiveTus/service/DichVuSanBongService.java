package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DichVuSanBongDTO;
import com.example.DATN_WebFiveTus.entity.DichVuSanBong;

import java.util.List;

public interface DichVuSanBongService {

    List<DichVuSanBongDTO> getAll();

    DichVuSanBongDTO getOne(Integer id);

    DichVuSanBongDTO save(DichVuSanBongDTO dichVuSanBongDTO);

    DichVuSanBongDTO update(Integer id, DichVuSanBongDTO dichVuSanBongDTO);

    void delete(Integer id);

    List<DichVuSanBongDTO> searchDichVuSanBong(Integer idHoaDonChiTiet);

    List<DichVuSanBongDTO> getAllJoinFetch();
}
