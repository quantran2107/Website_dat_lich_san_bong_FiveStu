package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;

import java.util.List;

public interface DoThueService {
    List<DoThueDTO> getAll();

    DoThueDTO getOne(Integer id);

    DoThueDTO save(DoThueDTO doThueDTO);

    DoThueDTO update(Integer id,DoThueDTO doThueDTO);

    void delete (Integer id);
    
}
