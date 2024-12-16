package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ChiTietHinhThucThanhToanDTO;
import com.example.DATN_WebFiveTus.dto.HTTTDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CTHTTTService {
//    List<HTTTDto> getHtttById(int id);

//    Boolean addNew(HTTTDto htttDto);

    List<ChiTietHinhThucThanhToanDTO> findByHoaDonChiTietId(int hoaDonChiTietId);

    void deletedSoft(int idHinhThucThanhToan);
    ChiTietHinhThucThanhToanDTO save(ChiTietHinhThucThanhToanDTO chiTietHinhThucThanhToanDTO, HttpServletRequest request);
}
