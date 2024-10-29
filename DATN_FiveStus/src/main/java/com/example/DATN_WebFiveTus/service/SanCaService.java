package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.SanCa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface SanCaService {

    List<SanCaDTO> getAll();

    List<SanCaDTO> getAllJoinFetch();

    SanCaDTO getOne(Integer id);

    SanCaDTO save(SanCaDTO sanCaDTO);

    SanCaDTO update(Integer id, SanCaDTO sanCaDTO);

    void delete(Integer id);

    void deletedAt(Integer id);

    public List<SanCaDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement);

    public List<SanCaDTO> searchKeyWords(Integer pageNum, String keyWords, String sortDirection, int[] totalPageElement, Integer id);

    List<SanCaDTO> findByTrangThai(Integer idCa, String thuTrongTuan ,String trangThai );


    List<SanCaDTO> hienThiSanTrong(Integer idLoaiSan, List<String> thuTrongTuanList, java.util.Date startDate, Date endDate);


    List<SanCaDTO> findSanCaBySan(@Param("idSanBong") Integer idSanBong,
                                  @Param("idNgayTrongTuan") Integer idNgayTrongTuan);

    SanCaDTO getOneSanCaByAll(@Param("idSanBong") Integer idSanBong,
                                    @Param("idNgayTrongTuan") Integer idNgayTrongTuan,
                                    @Param("idCa") Integer idCa);

    //Ly them
    List<SanCaDTO> findSanCaByNhieuNgay(@Param("idSanBong") Integer idSanBong,
                                  @Param("listIdNgayTrongTuan") List<Integer> listIdNgayTrongTuan);


    void addSanCaForSanBongWithIdLoaiSan2(int idLoaiSan, SanCaDTO sanCaDTO);

    List<SanCaDTO> getListSanCaExits(Integer idLoaiSan, List<Integer>  idSanBong, List<Integer> idNgayTrongTuan, List<Integer> idCa);

    List<SanCaDTO> getAllSanCaByLoaiSan(@Param("idLoaiSan") Integer idLoaiSan,
                                     @Param("idNgayTrongTuan") Integer idNgayTrongTuan,
                                     @Param("idCa") Integer idCa);


    ResponseEntity<ApiResponseDto<?>> findForCustomer(Integer id);
}
