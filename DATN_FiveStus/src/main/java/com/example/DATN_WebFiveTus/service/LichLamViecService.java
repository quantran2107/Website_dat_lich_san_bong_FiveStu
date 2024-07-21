package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.LichLamViecDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LichLamViecService {
    List<LichLamViecDTO> getAll();

    Boolean add(LichLamViecDTO lichLamViecDto);

    Boolean addMore(MultipartFile file);
}
