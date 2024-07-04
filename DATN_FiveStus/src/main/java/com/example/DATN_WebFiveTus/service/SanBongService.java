package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SanBongService {

    List<SanBongDTO> getAll();

    SanBongDTO getOne(Integer id);

    SanBongDTO save(SanBongDTO sanBongDTO);

    SanBongDTO update(Integer id, SanBongDTO sanBongDTO);

    void delete (Integer id);

    List<SanBongDTO> getAllJoinFetch();

    void deletedAt(Integer id);

    Page<SanBongDTO> pages(Integer pageNo, Integer pageSize);

    List<SanBongDTO> getSanBongsByLoaiSanId(Integer loaiSanId);
}
