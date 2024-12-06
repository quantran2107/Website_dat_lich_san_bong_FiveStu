package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonService {

    List<HoaDonDTO> getAll();

    List<HoaDonDTO> getAllJoinFetch();

    HoaDonDTO getOne(Integer id);

    HoaDonDTO save(HoaDonDTO hoaDonDTO);

    void updateThanhToan(Integer idHoaDonChiTiet);

    HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO);

    Page<HoaDonDTO> phanTrang(Pageable pageable);

    void delete(Integer id);

    void deletedAt(Integer id);

    void sendInvoiceEmail(HoaDonDTO hoaDonDTO, List<HoaDonChiTietDTO> hoaDonChiTietList);

    void updateTrangThaiHoaDon(Integer idHoaDon, String trangThai);

    List<HoaDonDTO> getHDforNV(int id);


    Page<HoaDonDTO> searchAndFilter(@Param("loai") Boolean loai,
                                    @Param("trangThai") String trangThai,
                                    @Param("keyword") String keyword,
                                    @Param("tongTienMin") Float tongTienMin,
                                    @Param("tongTienMax") Float tongTienMax,
                                    @Param("ngayTaoMin") LocalDateTime ngayTaoMin,
                                    @Param("ngayTaoMax") LocalDateTime ngayTaoMax,
                                    Pageable pageable);

    HoaDonDTO huyLichDat(Integer id);

    NhanVienDTO getNhanVienTrongCa(HttpServletRequest request);
}
