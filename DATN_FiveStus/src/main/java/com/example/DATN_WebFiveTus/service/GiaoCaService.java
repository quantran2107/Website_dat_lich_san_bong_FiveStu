package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface GiaoCaService {

    GiaoCaDTO getRowforId(int id);

    Boolean addRow(GiaoCaFormRequest request);

    NhanVienDTO getIdNVG(HttpServletRequest request);

    Boolean changeGCN(GiaoCaRequest request);

    Boolean getLastRow(HttpServletRequest request);

    GiaoCaDTO checkNhanCa();

    NhanVienDTO getNV(HttpServletRequest request);
}
