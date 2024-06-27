package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.NuocUongDTO;

import java.util.List;

public interface NuocUongService {
    List<NuocUongDTO> getAll();

    NuocUongDTO getOne(Integer id);

    NuocUongDTO save(NuocUongDTO nuocUongDTO);

    NuocUongDTO update(Integer id,NuocUongDTO nuocUongDTO);

    void delete (Integer id);


}
