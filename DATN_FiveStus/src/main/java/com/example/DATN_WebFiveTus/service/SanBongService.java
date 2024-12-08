package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SanBongService {

    List<SanBongDTO> getAll();

    SanBongDTO getOne(Integer id);

    SanBongDTO save(SanBongDTO sanBongDTO);

    SanBongDTO update(Integer id, SanBongDTO sanBongDTO);

    void delete (Integer id);

    List<SanBongDTO> getAllJoinFetch();

    List<SanBongDTO> getAllJoinFetchActive();

    void deletedAt(Integer id);

    Page<SanBongDTO> pages(Integer pageNo, Integer pageSize);

    List<SanBongDTO> getSanBongsByLoaiSanId(Integer loaiSanId);


    public List<SanBongDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement);

    public List<SanBongDTO> searchKeyWords(Integer pageNum, String keyWords, String sortDirection, int[] totalPageElement, Integer id);

    SanBongDTO getSanBongByName(String tenSanBong);

    Boolean existsByTenSanBongs(Integer idLoaiSan,String tenSanBong);

    List<SanBongDTO> getListSanBongWithIdLoaiSan(Integer idLoaiSan);

    boolean checkTrungSanBong(Integer idLoaiSan, String tenSanBong);

    void updateTrangThai(Integer id, String status);
}
