package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NuocUongService {
    List<NuocUongDTO> getAll();

    NuocUongDTO getOne(Integer id);

    NuocUongDTO save(NuocUongDTO nuocUongDTO);

    NuocUongDTO update(Integer id,NuocUongDTO nuocUongDTO);

    void delete (Integer id);

    Page<NuocUongDTO> phanTrang(Pageable pageable);

    Page<NuocUongDTO> searchNuocUong(
            String keyword,
            String trangThai, Float donGiaMin, Float donGiaMax, Pageable pageable);

    List<NuocUongDTO> getAllJoinFetch2();

    Boolean checkIdDichVuNuocUong(Integer id, Integer idHoaDonChiTiet);

    int getIdNuocUong(Integer idNuocUong, Integer idHoaDonChiTiet );

    List<NuocUongDTO> searchTenNuocUong(String tenNuocUong);
}
