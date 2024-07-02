package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SanCaService {

    List<SanCaDTO> getAll();

    List<SanCaDTO> getAllJoinFetch();

    SanCaDTO getOne(Integer id);

    SanCaDTO save(SanCaDTO sanCaDTO);

    SanCaDTO update(Integer id, SanCaDTO sanCaDTO);

    void delete(Integer id);

    void deletedAt(Integer id);

    public List<SanCaDTO> listAll2(Integer pageNum, String sortDirection, int[] totalPageElement);
}
