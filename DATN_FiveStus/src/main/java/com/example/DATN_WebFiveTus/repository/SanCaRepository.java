package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.SanCa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface SanCaRepository extends JpaRepository<SanCa,Integer> {
    @Query("SELECT sc FROM SanCa sc " +
            "JOIN FETCH sc.ca " +
            "JOIN FETCH sc.sanBong " +
            "JOIN FETCH sc.ngayTrongTuan where sc.deletedAt=false")
    List<SanCa> getAllJoinFetch();

    @Modifying
    @Transactional
    @Query("UPDATE SanCa sc SET sc.deletedAt = TRUE WHERE sc.id = :id")
    void deletedAt(Integer id);


    @Query("SELECT sc FROM SanCa sc")
    Page<SanCa> findBySanCaPage(Pageable  pageable);

    @Query("SELECT sc FROM SanCa sc WHERE sc.id = :id OR sc.sanBong.tenSanBong=:keyWords " +
            "OR sc.ca.tenCa=:keyWords OR sc.ngayTrongTuan.thuTrongTuan=:keyWords OR sc.trangThai=:keyWords")
    Page<SanCa> search(@Param("id") Integer id, @Param("keyWords") String keyWords, Pageable pageable);

    @Query("select sc from SanCa sc " +
            "join fetch sc.sanBong sb " +
            "join fetch sc.ca c " +
            "join fetch sc.ngayTrongTuan ntt " +
            "where sc.deletedAt = false and sb.deletedAt = false and c.deletedAt = false " +
            "and c.id = :idCa and ntt.thuTrongTuan = :thuTrongTuan and sc.trangThai = :trangThai")
    List<SanCa> sanHopLe(@Param("idCa") Integer idCa, @Param("thuTrongTuan") String thuTrongTuan, @Param("trangThai") String trangThai);


    @Query("select sc from SanCa sc " +
            "join fetch sc.sanBong sb " +
            "join fetch sc.ca c " +
            "join fetch sc.ngayTrongTuan ntt " +
            "where sc.deletedAt = false " +
            "and sb.deletedAt = false " +
            "and c.deletedAt = false " +
            "and sc.trangThai = 'Còn trống' " +
            "and sb.loaiSan.id = :idLoaiSan " +
            "and ntt.thuTrongTuan in :thuTrongTuanList " +  // So sánh các thứ trong tuần
            "and not exists (" +
            "    select hdct from HoaDonChiTiet hdct " +
            "    where hdct.sanCa.id = sc.id " +
            "    and hdct.ngayDenSan between :startDate and :endDate)")
    List<SanCa> hienThiSanTrong(@Param("idLoaiSan") Integer idLoaiSan,
                                @Param("thuTrongTuanList") List<String> thuTrongTuanList,  // Danh sách các thứ trong tuần
                                @Param("startDate") java.util.Date startDate,
                                @Param("endDate") Date endDate);


//    @Query("SELECT new com.example.DATN_WebFiveTus.dto.SanCaDTO(" +
//            "sc.id, sc.gia, c.id, c.tenCa, sb.id, sb.tenSanBong, " +
//            "nt.id, nt.thuTrongTuan, sc.trangThai, ls.tenLoaiSan, " +
//            "c.thoiGianBatDau, c.thoiGianKetThuc, hdct.ngayDenSan) " +
//            "FROM SanCa sc " +
//            "JOIN sc.sanBong sb " +
//            "JOIN sc.ca c " +
//            "JOIN sc.ngayTrongTuan nt " +
//            "JOIN sb.loaiSan ls " +
//            "LEFT JOIN HoaDonChiTiet hdct ON hdct.sanCa.id = sc.id AND hdct.id = :id")
//    List<SanCaDTO> findSanCaAndNgayDenSanByHoaDonChiTietId(@Param("id") Integer id);

    @Query("SELECT sc FROM SanCa sc " +
            "WHERE sc.sanBong.id = :idSanBong " +
            "AND sc.ngayTrongTuan.id = :idNgayTrongTuan")
    List<SanCa> findSanCaBySan(@Param("idSanBong") Integer idSanBong,
                               @Param("idNgayTrongTuan") Integer idNgayTrongTuan);

    @Query("SELECT sc FROM SanCa sc " +
            "WHERE sc.sanBong.id = :idSanBong " +
            "AND sc.ngayTrongTuan.id = :idNgayTrongTuan " +
            "AND sc.ca.id = :idCa ")
    SanCa getOneSanCaByAll(@Param("idSanBong") Integer idSanBong,
                               @Param("idNgayTrongTuan") Integer idNgayTrongTuan,
                               @Param("idCa") Integer idCa);

    //Ly them
    @Query("SELECT sc FROM SanCa sc " +
            "WHERE sc.sanBong.id = :idSanBong " +
            "AND sc.ngayTrongTuan.id IN :listIdNgayTrongTuan")
    List<SanCa> findSanCaByNhieuNgay(@Param("idSanBong") Integer idSanBong,
                                     @Param("listIdNgayTrongTuan") List<Integer> listIdNgayTrongTuan);

    @Query("SELECT sc FROM SanCa sc " +
            "WHERE sc.sanBong.loaiSan.id = :idLoaiSan " +
            "AND sc.sanBong.id IN :idSanBong " +
            "AND sc.ngayTrongTuan.id IN :idNgayTrongTuan " + // Sử dụng IN cho danh sách
            "AND sc.ca.id IN :idCa") // Sử dụng IN cho danh sách
    List<SanCa> getListSanCaExits(@Param("idLoaiSan") Integer idLoaiSan,
                                  @Param("idSanBong") List<Integer>  idSanBong,
                                  @Param("idNgayTrongTuan") List<Integer> idNgayTrongTuan,
                                  @Param("idCa") List<Integer> idCa);




    @Query("SELECT sc FROM SanCa  sc WHERE sc.ngayTrongTuan.id = :idNgayTrongTuan " +
                    "AND sc.ca.id = :idCa " +
                    "AND sc.sanBong.loaiSan.id = :idLoaiSan")
    List<SanCa> getAllSanCaByLoaiSan(@Param("idLoaiSan") Integer idLoaiSan,
                                     @Param("idNgayTrongTuan") Integer idNgayTrongTuan,
                                     @Param("idCa") Integer idCa);


}
