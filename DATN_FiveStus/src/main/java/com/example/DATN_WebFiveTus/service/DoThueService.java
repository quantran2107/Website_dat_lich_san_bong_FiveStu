package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

public interface DoThueService {
    List<DoThueDTO> getAll();

    DoThueDTO getOne(Integer id);

    DoThueDTO save(DoThueDTO doThueDTO);

    DoThueDTO update(Integer id,DoThueDTO doThueDTO);

    void delete (Integer id);

    Page<DoThueDTO> phanTrang(Pageable pageable);

    Page<DoThueDTO> searchDoThue(
            String keyword,
            String trangThai, Float donGiaMin, Float donGiaMax, Pageable pageable);

}
