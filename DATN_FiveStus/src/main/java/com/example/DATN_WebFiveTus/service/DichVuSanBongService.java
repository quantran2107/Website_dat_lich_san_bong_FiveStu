package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.DichVuSanBongDTO;
import com.example.DATN_WebFiveTus.entity.DichVuSanBong;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DichVuSanBongService {

    List<DichVuSanBongDTO> getAll();

    DichVuSanBongDTO getOne(Integer id);

    DichVuSanBongDTO save(DichVuSanBongDTO dichVuSanBongDTO);

    DichVuSanBongDTO update(Integer id, DichVuSanBongDTO dichVuSanBongDTO);

    List<DichVuSanBongDTO> findByIdHDCT(Integer idHDCT);

    void delete(Integer id);

    List<DichVuSanBongDTO> searchDichVuSanBong(Integer idHoaDonChiTiet);

    List<DichVuSanBongDTO> getAllJoinFetch();

    DichVuSanBongDTO findDVSBbyIdHDCTandIdDoThue(@Param("idHDCT") Integer idHDCT,
                                                       @Param("idDoThue") Integer idDoThue);

    DichVuSanBongDTO findDVSBbyIdHDCTandIdNuocUong(@Param("idHDCT") Integer idHDCT,
                                                         @Param("idNuocUong") Integer idNuocUong);
}
