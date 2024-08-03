package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.entity.ThamSo;

import java.util.List;

public interface ThamSoService {

     List<ThamSoDTO> getAll();

     ThamSoDTO getOne(Integer id);

     ThamSoDTO save(ThamSoDTO thamSoDTO);

     ThamSoDTO update(ThamSoDTO thamSoDTO, Integer id);

     void delete(Integer id);

     ThamSoDTO findByTenThamSo(String ma);

}
