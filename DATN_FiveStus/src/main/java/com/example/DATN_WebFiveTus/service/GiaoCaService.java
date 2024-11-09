package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.request.NhanCaRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface GiaoCaService {


    GiaoCaDTO lastData();

    ApiResponseDto<?> banGiao(HttpServletRequest request);

    Boolean addRow(HttpServletRequest request, NhanCaRequest requestBody);

    ApiResponseDto<?> checkGC(HttpServletRequest request);
}
