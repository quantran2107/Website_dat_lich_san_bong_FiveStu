package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
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

    KhachHangDTO findBySoDienThoai(String soDienThoai);

    Page<KhachHangDTO> getAll(Pageable pageable);

    Page<KhachHangDTO> searchAndFilter(String query, String status, String gender, int page, int pageSize);

    Page<KhachHangDTO> filter(String status, String gender, int page, int pageSize);

    Page<KhachHangDTO> searchActive(String query, String trangThai, Pageable pageable);

    KhachHangDTO save2(KhachHangDTO khachHangDTO);

    KhachHangDTO updateKhachHangByEmail(String email, KhachHangDTO khachHangDTO);

    boolean isEmailExists(String email);

    boolean isSoDienThoaiExists(String soDienThoai);

    boolean isMaKhachHangExists(String maKhachHang);
}

