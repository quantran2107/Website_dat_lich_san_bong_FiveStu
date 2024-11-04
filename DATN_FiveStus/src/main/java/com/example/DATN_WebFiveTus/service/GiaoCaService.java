package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import com.example.DATN_WebFiveTus.dto.request.NhanCaRequest;
import com.example.DATN_WebFiveTus.dto.response.BanGiaoCaResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface GiaoCaService {


    GiaoCaDTO lastData();

    BanGiaoCaResponse banGiao(HttpServletRequest request);

    ResponseEntity<?> changeGC(HttpServletRequest request, GiaoCaFormRequest giaoCaFormRequest);

    Boolean addRow(HttpServletRequest request, NhanCaRequest requestBody);
}
