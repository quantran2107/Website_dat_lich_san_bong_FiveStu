package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.LichLamViecDTO;
import com.example.DATN_WebFiveTus.dto.request.LichLamViecSearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LichLamViecService {
    List<LichLamViecDTO> getAll(String date, String status );

    Boolean add(LichLamViecDTO lichLamViecDto);

    Boolean addMore(MultipartFile file);
}
