package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;

import java.util.List;

public interface CaService {

    List<CaDTO> getAll();

    CaDTO getOne(Integer id);

    CaDTO save(CaDTO caDTO);

    CaDTO update(Integer id, CaDTO caDTO);


    void delete (Integer id);

    void  deletedAt(Integer id);

    List<CaDTO> getAllJoinFetch();


    public List<CaDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement);

    public List<CaDTO> searchKeyWords(Integer pageNum, String keyWords, String sortDirection, int[] totalPageElement, Integer id);
}
