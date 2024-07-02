package com.example.DATN_WebFiveTus.service;


import com.example.DATN_WebFiveTus.dto.NhanVienDTO;

import java.util.List;

public interface NhanVienService {
    List<NhanVienDTO> getAll();

    void save(NhanVienDTO nhanVienDTO);

    void updateNV(int id, NhanVienDTO nhanVienDTO);
}
