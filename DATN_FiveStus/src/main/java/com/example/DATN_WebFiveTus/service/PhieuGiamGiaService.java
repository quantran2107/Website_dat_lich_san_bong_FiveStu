package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PhieuGiamGiaService {
    List<PhieuGiamGiaDTO> getAll();

    PhieuGiamGiaDTO getOne(Integer id);

    PhieuGiamGiaDTO save(PhieuGiamGiaDTO phieuGiamGiaDTO);

    PhieuGiamGiaDTO update(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO);

    Page<PhieuGiamGiaDTO> phanTrang(Pageable pageable);

    void delete(Integer id);

    void updateStatus(Integer id, boolean newStatus);

    void deleteMultiple(List<Integer> ids);

    public List<PhieuGiamGiaDTO> search(String query);

    public List<PhieuGiamGiaDTO> filter(String status);
}
