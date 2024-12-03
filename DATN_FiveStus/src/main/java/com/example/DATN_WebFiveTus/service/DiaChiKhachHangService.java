package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DiaChiKhachHangService {
    List<DiaChiKhachHangDTO> getAll();

    DiaChiKhachHangDTO getOne(Integer id);

    void save(DiaChiKhachHangDTO diaChiKhachHangDTO);

    void update(Integer id, DiaChiKhachHangDTO diaChiKhachHangDTO);

    void delete (Integer id);

    List<DiaChiKhachHangDTO> getAllJoinFetch();

    List<DiaChiKhachHangDTO> findById(Integer id);

    DiaChiKhachHangDTO updateDiaChi(Integer diaChiId, DiaChiKhachHangDTO diaChiKhachHangDTO);

    Page<DiaChiKhachHangDTO> findByIdDC(Integer id, Pageable pageable);

    public List<DiaChiKhachHangDTO> getDiaChiByEmail(String email);

    public boolean deleteDiaChiByEmail(String email, Integer id);

    public DiaChiKhachHangDTO updateDiaChiByEmail(String email, Integer id, DiaChiKhachHangDTO diaChiKhachHangDTO);
    public Optional<DiaChiKhachHang> getDiaChiByEmailAndId(String email, Integer id);

    void addDiaChiByEmail(String email, DiaChiKhachHangDTO diaChiKhachHangDTO);

}
