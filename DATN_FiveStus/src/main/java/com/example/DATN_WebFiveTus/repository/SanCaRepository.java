package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.SanCa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SanCaRepository extends JpaRepository<SanCa,Integer> {
    @Query("SELECT sc FROM SanCa sc " +
            "JOIN FETCH sc.ca " +
            "JOIN FETCH sc.sanBong " +
            "JOIN FETCH sc.ngayTrongTuan where sc.deletedAt=true")
    List<SanCa> getAllJoinFetch();

    @Modifying
    @Transactional
    @Query("UPDATE SanCa sc SET sc.deletedAt = FALSE WHERE sc.id = :id")
    void deletedAt(Integer id);

    @Query("SELECT sc FROM SanCa sc")
    List<SanCa> findBySanCaSort(Sort sort);

    @Query("SELECT sc FROM SanCa sc")
    Page<SanCa> findBySanCaPage(Pageable  pageable);
}
