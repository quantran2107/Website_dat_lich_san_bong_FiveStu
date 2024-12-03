package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DoThueService {
    List<DoThueDTO> getAll();

    DoThueDTO getOne(Integer id);

    DoThueDTO save(DoThueDTO doThueDTO);

    DoThueDTO update(Integer id, DoThueDTO doThueDTO);

    void delete (Integer id);

    Page<DoThueDTO> phanTrang(Pageable pageable);

    Page<DoThueDTO> searchDoThue(
            String keyword,
            String trangThai, Float donGiaMin, Float donGiaMax, Pageable pageable);

    List<DoThueDTO> getAllJoinFetch2();

     Boolean checkIdDichVuDoThue(Integer id, Integer idHoaDonChiTiet);

    int getIdDoThue(Integer idDoThue, Integer idHoaDonChiTiet );

    List<DoThueDTO> searchTenDoThue(String tenDoThue);

    DoThueDTO updateSoLuong(Integer id,DoThueDTO doThueDTO);

}
