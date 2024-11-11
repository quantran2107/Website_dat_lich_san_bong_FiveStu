package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface HoaDonChiTietService {

    List<HoaDonChiTietDTO> getAll();

    List<HoaDonChiTietDTO> getAllJoinFetch();

    HoaDonChiTietDTO getOne(Integer id);

    HoaDonChiTietDTO save(HoaDonChiTietDTO hoaDonChiTietDTO);

    HoaDonChiTietDTO update(Integer id, HoaDonChiTietDTO hoaDonChiTietDTO);

    void delete (Integer id);

    void deletedAt(Integer id);

    List<HoaDonChiTietDTO> searchFromHoaDon(Integer idHoaDon);

    Page<HoaDonChiTietDTO> getHoaDonChiTietByTrangThai(String trangThai, String soDienThoaiKhachHang, Pageable pageable);

    HoaDonChiTietDTO getOneHDCT(Integer id);


   void updateTrangThai(Integer id);

   void updateTrangThaiThanhToan(Integer id);

    List<HoaDonChiTietDTO> findByNgayDenSan(Date ngayDenSan);

    HoaDonChiTietDTO save2(HoaDonChiTietDTO hoaDonChiTietDTO);


     boolean isSanCaBooked(Long idSanCa, LocalDate ngayDenSan);

    List<HoaDonChiTietDTO> findByIdKhachHang(Integer id);

    Boolean huyDatSan(Integer id);
}
