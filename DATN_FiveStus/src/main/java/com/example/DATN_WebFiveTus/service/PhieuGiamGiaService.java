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

    //
    PhieuGiamGiaDTO save(PhieuGiamGiaDTO phieuGiamGiaDTO);

    PhieuGiamGiaDTO update(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO);


    //    void delete(Integer id);
//
    void updateStatus(Integer id, String newStatus);
//
//    void deleteMultiple(List<Integer> ids);
//
//    public List<PhieuGiamGiaDTO> search(String query);
//
//    public List<PhieuGiamGiaDTO> filter(String status);

    Page<PhieuGiamGiaDTO> searchPhieuGiamGia(
            String keyword, Boolean doiTuongApDung, Boolean hinhThucGiamGia,
            String trangThai, Date ngayBatDau, Date ngayKetThuc, Pageable pageable);
}
