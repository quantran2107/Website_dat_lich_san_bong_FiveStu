package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.NgayTrongTuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NgayTrongTuanRepository extends JpaRepository<NgayTrongTuan,Integer> {
    @Query("SELECT n FROM NgayTrongTuan n WHERE n.thuTrongTuan = :thuTrongTuan")
    NgayTrongTuan findNgayTrongTuanByThuTrongTuan(@Param("thuTrongTuan") String thuTrongTuan);
}
