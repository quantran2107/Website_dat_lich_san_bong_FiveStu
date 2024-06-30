package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;

import java.util.List;

public interface KhachHangService {

    List<KhachHangDTO> getAll();

    KhachHangDTO getOne(Integer id);

    KhachHangDTO save(KhachHangDTO khachHangDTO);

    KhachHangDTO update(Integer id, KhachHangDTO KhachHangDTO);

    void delete(Integer id);

    KhachHangDTO findById(Integer id);
}
