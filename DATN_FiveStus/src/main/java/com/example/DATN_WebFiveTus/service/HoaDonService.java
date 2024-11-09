package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonRequest;
import com.example.DATN_WebFiveTus.dto.PaymentResponse;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HoaDonService {

    List<HoaDonDTO> getAll();

    List<HoaDonDTO> getAllJoinFetch();

    HoaDonDTO getOne(Integer id);

    HoaDonDTO save(HoaDonDTO hoaDonDTO);

    void updateThanhToan(Integer idHoaDonChiTiet);

    HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO);

    Page<HoaDonDTO> phanTrang(Pageable pageable);

    void delete (Integer id);

    void deletedAt(Integer id);

    List<HoaDonDTO> getHDforNV(int id);

    PaymentResponse confirmPayment(String maHoaDon);


    Page<HoaDonDTO> searchAndFilter(@Param("loai") Boolean loai,
                                    @Param("trangThai") String trangThai,
                                    @Param("keyword") String keyword,
                                    @Param("tongTienMin") Float tongTienMin,
                                    @Param("tongTienMax") Float tongTienMax,
                                    Pageable pageable);

    HoaDonDTO huyLichDat(Integer id);
}
