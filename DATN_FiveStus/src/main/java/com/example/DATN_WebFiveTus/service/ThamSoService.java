package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.entity.ThamSo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThamSoService {

     List<ThamSoDTO> getAll();

     ThamSoDTO getOne(Integer id);

     ThamSoDTO save(ThamSoDTO thamSoDTO);

     ThamSoDTO update(ThamSoDTO thamSoDTO, Integer id);

     void delete(Integer id);

     ThamSoDTO findByTenThamSo(String ma);

      List<ThamSoDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement);

     Page<ThamSoDTO> searchThamSo(String keyword , Pageable pageable);

     Page<ThamSoDTO> searchThamSoss(String ma, String ten, String typeGiaTri, Boolean trangThai, Pageable pageable);
}
