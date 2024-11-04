package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;

import java.util.List;

public interface NgayTrongTuanService {

    List<NgayTrongTuanDTO> getAll();

    NgayTrongTuanDTO getOne(Integer id);

    NgayTrongTuanDTO save(NgayTrongTuanDTO ngayTrongTuanDTO);

    NgayTrongTuanDTO update(Integer id,NgayTrongTuanDTO ngayTrongTuanDTO);

    void delete (Integer id);

    NgayTrongTuanDTO findByNgayTrongTuan(String ngayTrongTuan);
}
