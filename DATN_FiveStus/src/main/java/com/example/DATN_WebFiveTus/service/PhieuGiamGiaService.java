package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface PhieuGiamGiaService {

    Page<PhieuGiamGiaDTO> phanTrang(Pageable pageable);

    List<PhieuGiamGiaDTO> getAll();

    List<PhieuGiamGiaDTO> fillPGG(Double tongTien);

    PhieuGiamGiaDTO getOne(Integer id);

    PhieuGiamGiaDTO save(PhieuGiamGiaDTO phieuGiamGiaDTO);

    PhieuGiamGiaDTO update(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO);

    PhieuGiamGiaDTO update2(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO);

    void updateStatus(Integer id, String newStatus);

    Page<PhieuGiamGiaDTO> searchPhieuGiamGia(
            String keyword, Boolean doiTuongApDung, Boolean hinhThucGiamGia,
            String trangThai, Date ngayBatDau, Date ngayKetThuc, Pageable pageable);

    boolean isDuplicateMaPhieuGiamGia(String maPhieuGiamGia);


}
