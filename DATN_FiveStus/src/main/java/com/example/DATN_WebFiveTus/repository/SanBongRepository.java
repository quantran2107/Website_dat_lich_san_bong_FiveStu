package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.entity.SanCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SanBongRepository extends JpaRepository<SanBong,Integer> {

    @Query("SELECT sb FROM SanBong sb where sb.deletedAt=true")
    List<SanBong> getAllJoinFetch();

    @Modifying
    @Transactional
    @Query("UPDATE SanBong sb SET sb.deletedAt = FALSE WHERE sb.id = :id")
    void deletedAt(Integer id);
}
