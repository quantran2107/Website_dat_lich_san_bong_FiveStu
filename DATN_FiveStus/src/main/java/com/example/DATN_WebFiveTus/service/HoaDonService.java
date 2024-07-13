package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonService {

    List<HoaDonDTO> getAll();

    List<HoaDonDTO> getAllJoinFetch();

    HoaDonDTO getOne(Integer id);

    HoaDonDTO save(HoaDonDTO hoaDonDTO);

    HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO);

    Page<HoaDonDTO> phanTrang(Pageable pageable);

    void delete (Integer id);

    void deletedAt(Integer id);

    Page<HoaDonDTO> searchAndFilter(@Param("loai") Boolean loai,
                                    @Param("keyword") String keyword,
                                    @Param("tongTienMin") Float tongTienMin,
                                    @Param("tongTienMax") Float tongTienMax,
                                    Pageable pageable);
}
