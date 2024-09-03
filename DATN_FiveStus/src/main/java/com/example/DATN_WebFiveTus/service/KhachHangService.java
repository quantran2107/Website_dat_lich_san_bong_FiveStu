package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface KhachHangService {

    List<KhachHangDTO> getAll();

    KhachHangDTO getOne(Integer id);

    KhachHangDTO save(KhachHangDTO khachHangDTO);

    void update(Integer id, KhachHangDTO khachHangDTO);

    void delete(Integer id);

    KhachHangDTO findById(Integer id);

    Page<KhachHangDTO> getAll(Pageable pageable);

    public List<KhachHangDTO> search(String query, int page, int pageSize);

    public List<KhachHangDTO> filter(String status, String gender, int page, int pageSize);

}

