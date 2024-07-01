package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.entity.GiaoCa;

import java.util.List;

public interface GiaoCaService {
   List<GiaoCaDTO> getAll();

    void save(GiaoCaDTO giaoCaDTO);
}
