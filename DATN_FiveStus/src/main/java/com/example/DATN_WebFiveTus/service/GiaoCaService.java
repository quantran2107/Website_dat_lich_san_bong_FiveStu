package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import com.example.DATN_WebFiveTus.entity.GiaoCa;

import java.util.List;

public interface GiaoCaService {

    GiaoCaDTO getRowforId(int id);

    Boolean changeGCN(int id, GiaoCaRequest request);

    GiaoCaDTO getLastRow();

    Boolean addRow(GiaoCaFormRequest request);

    Integer getIdNVG();
}
